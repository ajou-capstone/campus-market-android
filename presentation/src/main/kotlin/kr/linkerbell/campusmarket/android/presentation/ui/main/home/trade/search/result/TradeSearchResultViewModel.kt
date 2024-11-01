package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result

import android.util.Log
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
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.SearchTradeListUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class TradeSearchResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val searchTradeListUseCase: SearchTradeListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<TradeSearchResultState> =
        MutableStateFlow(TradeSearchResultState.Init)
    val state: StateFlow<TradeSearchResultState> = _state.asStateFlow()

    private val _event: MutableEventFlow<TradeSearchResultEvent> = MutableEventFlow()
    val event: EventFlow<TradeSearchResultEvent> = _event.asEventFlow()

    private val _tradeList: MutableStateFlow<PagingData<Trade>> =
        MutableStateFlow(PagingData.empty())
    val tradeList: StateFlow<PagingData<Trade>> = _tradeList.asStateFlow()

    private val _tradeSearchQuery: MutableStateFlow<TradeSearchQuery> =
        MutableStateFlow(TradeSearchQuery())
    val tradeSearchQuery: StateFlow<TradeSearchQuery> = _tradeSearchQuery.asStateFlow()

    fun onIntent(intent: TradeSearchResultIntent) {

    }

    suspend fun onReceivedQuery(receivedQuery: TradeSearchQuery) {
        Log.d("siri22","QueryUpdated")
        _state.value = TradeSearchResultState.Loading
        _tradeSearchQuery.value = receivedQuery

        searchTradeListUseCase(
            name = _tradeSearchQuery.value.name,
            category = _tradeSearchQuery.value.category,
            minPrice = _tradeSearchQuery.value.minPrice,
            maxPrice = _tradeSearchQuery.value.maxPrice,
            sorted = _tradeSearchQuery.value.sorted
        )
            .cachedIn(viewModelScope)
            .catch { exception ->
                when (exception) {
                    is ServerException -> {
                        _state.value = TradeSearchResultState.Init
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }
                    else -> {
                        _state.value = TradeSearchResultState.Init
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }.collect {
                _state.value = TradeSearchResultState.Init
                _tradeList.value = it
            }
    }

    fun updateTradeSearchQuery(newQuery: TradeSearchQuery) {
        _tradeSearchQuery.value = newQuery
    }

}
