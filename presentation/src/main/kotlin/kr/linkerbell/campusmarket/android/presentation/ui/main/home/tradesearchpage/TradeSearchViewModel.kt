package kr.linkerbell.campusmarket.android.presentation.ui.main.home.tradesearchpage

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tradesearch.GetSearchHistory
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel

@HiltViewModel
class TradeSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSearchHistory: GetSearchHistory
) : BaseViewModel() {

    private val _state: MutableStateFlow<TradeSearchState> = MutableStateFlow(TradeSearchState.Init)
    val state: StateFlow<TradeSearchState> = _state.asStateFlow()

    private val _event: MutableEventFlow<TradeSearchEvent> = MutableEventFlow()
    val event: EventFlow<TradeSearchEvent> = _event.asEventFlow()

    private val _searchHistory: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    fun onIntent(intent: TradeSearchIntent) {

    }

    private suspend fun fetchSearchHistory() {
        _state.value = TradeSearchState.Loading
        getSearchHistory().onSuccess { searchHistory ->
            _state.value = TradeSearchState.Init
            _searchHistory.value = searchHistory.searchHistory
        }.onFailure {
            _state.value = TradeSearchState.Init
            _searchHistory.value = emptyList()
        }
    }
}
