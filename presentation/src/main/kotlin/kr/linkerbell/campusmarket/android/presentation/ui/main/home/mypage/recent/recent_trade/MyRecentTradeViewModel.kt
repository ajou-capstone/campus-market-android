package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.GetRecentTradeListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.RateUserUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeEvent
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeIntent
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.MyRecentTrade.my_recent_trade.MyRecentTradeState
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile.recent.trade.RecentTradeConstant

@HiltViewModel
class MyRecentTradeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecentTradeListUseCase: GetRecentTradeListUseCase,
    private val rateUserUseCase: RateUserUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<MyRecentTradeState> =
        MutableStateFlow(MyRecentTradeState.Init)
    val state: StateFlow<MyRecentTradeState> = _state.asStateFlow()

    private val _event: MutableEventFlow<MyRecentTradeEvent> = MutableEventFlow()
    val event: EventFlow<MyRecentTradeEvent> = _event.asEventFlow()

    private val _recentBuyTrades: MutableStateFlow<PagingData<RecentTrade>> =
        MutableStateFlow(PagingData.empty())
    val recentBuyTrades: StateFlow<PagingData<RecentTrade>> =
        _recentBuyTrades.asStateFlow()

    private val _recentSellTrades: MutableStateFlow<PagingData<RecentTrade>> =
        MutableStateFlow(PagingData.empty())
    val recentSellTrades: StateFlow<PagingData<RecentTrade>> =
        _recentSellTrades.asStateFlow()

    private val _recentTrades: MutableStateFlow<PagingData<RecentTrade>> =
        MutableStateFlow(PagingData.empty())
    val recentTrades: StateFlow<PagingData<RecentTrade>> =
        _recentTrades.asStateFlow()

    private val _userId: MutableStateFlow<Long> = MutableStateFlow(
        savedStateHandle.get<Long>(RecentTradeConstant.ROUTE_ARGUMENT_USER_ID) ?: -1L
    )
    val userId: StateFlow<Long> = _userId.asStateFlow()

    init {
        launch {
            getAllTradeHistory(_userId.value)
        }
    }

    fun onIntent(intent: MyRecentTradeIntent) {
        when (intent) {
            is MyRecentTradeIntent.RefreshAllTradeList -> {
                launch {
                    getAllTradeHistory(_userId.value)
                }
            }

            is MyRecentTradeIntent.RefreshSellTradeList -> {
                launch {
                    getSalesTradeHistory(_userId.value)
                }
            }

            is MyRecentTradeIntent.RefreshBuyTradeList -> {
                launch {
                    getBuyTradeHistory(_userId.value)
                }
            }

            is MyRecentTradeIntent.RateUser -> {
                launch {
                    rateUser(
                        intent.targetUserId,
                        intent.itemId,
                        intent.description,
                        intent.rating
                    )
                }
            }

            MyRecentTradeIntent.RefreshTradeList -> {
                launch {
                    refreshAllHistory(_userId.value)
                }
            }
        }
    }

    private suspend fun refreshAllHistory(userId: Long) = coroutineScope {

        val getSalesTradeHistoryJob = async { getSalesTradeHistory(userId) }
        val getBuyTradeHistoryJob = async { getBuyTradeHistory(userId) }
        val getAllTradeHistoryJob = async { getAllTradeHistory(userId) }

        getSalesTradeHistoryJob.await()
        getBuyTradeHistoryJob.await()
        getAllTradeHistoryJob.await()
    }

    private suspend fun getSalesTradeHistory(userId: Long) {
        getRecentTradeListUseCase(userId, type = "sales")
            .cachedIn(viewModelScope)
            .catch { exception ->
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }.collect {
                _recentSellTrades.value = it
            }
    }

    private suspend fun getBuyTradeHistory(userId: Long) {
        getRecentTradeListUseCase(userId, type = "purchase")
            .cachedIn(viewModelScope)
            .catch { exception ->
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }.collect {
                _recentBuyTrades.value = it
            }
    }

    private suspend fun getAllTradeHistory(userId: Long) {
        getRecentTradeListUseCase(userId, type = "all")
            .cachedIn(viewModelScope)
            .catch { exception ->
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }.collect {
                _recentTrades.value = it
            }
    }

    private suspend fun rateUser(
        targetUserId: Long,
        itemId: Long,
        description: String,
        rating: Int
    ) {
        rateUserUseCase(
            targetUserId = targetUserId,
            itemId = itemId,
            description = description,
            rating = rating
        ).onSuccess {
            _state.value = MyRecentTradeState.Init
            _event.emit(MyRecentTradeEvent.RateSuccess)
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _event.emit(MyRecentTradeEvent.RateFail)
        }
    }
}
