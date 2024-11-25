package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class RecentTradeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRecentTradeListUseCase: GetRecentTradeListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<RecentTradeState> =
        MutableStateFlow(RecentTradeState.Init)
    val state: StateFlow<RecentTradeState> = _state.asStateFlow()

    private val _event: MutableEventFlow<RecentTradeEvent> = MutableEventFlow()
    val event: EventFlow<RecentTradeEvent> = _event.asEventFlow()

    private val _recentTrades: MutableStateFlow<PagingData<RecentTrade>> =
        MutableStateFlow(PagingData.empty())
    val recentTrades: StateFlow<PagingData<RecentTrade>> = _recentTrades.asStateFlow()

    init {
        launch {
            val userId =
                savedStateHandle.get<Long>(RecentTradeConstant.ROUTE_ARGUMENT_USER_ID) ?: -1L
            getOtherUserTradeHistory(userId)
        }
    }

    fun onIntent(intent: RecentTradeIntent) {

    }

    private suspend fun getOtherUserTradeHistory(userId: Long) {
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
                _recentTrades.value = it
            }
    }
}
