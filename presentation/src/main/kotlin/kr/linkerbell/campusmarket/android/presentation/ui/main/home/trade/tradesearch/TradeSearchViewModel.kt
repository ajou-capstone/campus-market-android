package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.tradesearch

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tradesearch.DeleteAllSearchHistoryByTextUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tradesearch.DeleteSearchHistoryByTextUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tradesearch.GetSearchHistoryUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tradesearch.InsertSearchHistoryUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel

@HiltViewModel
class TradeSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val deleteSearchHistoryByTextUseCase: DeleteSearchHistoryByTextUseCase,
    private val deleteAllSearchHistoryUseCase: DeleteAllSearchHistoryByTextUseCase,
    private val insertSearchHistoryUseCase: InsertSearchHistoryUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<TradeSearchState> = MutableStateFlow(TradeSearchState.Init)
    val state: StateFlow<TradeSearchState> = _state.asStateFlow()

    private val _event: MutableEventFlow<TradeSearchEvent> = MutableEventFlow()
    val event: EventFlow<TradeSearchEvent> = _event.asEventFlow()

    private val _searchHistory: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    suspend fun fetchSearchHistory() {
        _state.value = TradeSearchState.Loading
        getSearchHistoryUseCase().onSuccess { searchHistory ->
            _state.value = TradeSearchState.Init
            _searchHistory.value = searchHistory.searchHistory
        }.onFailure {
            _state.value = TradeSearchState.Init
            _searchHistory.value = emptyList()
        }
    }

    private suspend fun deleteSearchHistory(text: String) {
        deleteSearchHistoryByTextUseCase(text)
    }

    private suspend fun deleteAllSearchHistory() {
        deleteAllSearchHistoryUseCase()

    }

    private suspend fun insertSearchHistory(text: String) {
        insertSearchHistoryUseCase(text)

    }

    fun onIntent(intent: TradeSearchIntent) {
        when (intent) {
            is TradeSearchIntent.DeleteAll -> {
                launch {
                    deleteAllSearchHistory()
                    fetchSearchHistory()
                }
            }

            is TradeSearchIntent.DeleteByText -> {
                launch {
                    deleteSearchHistory(text = intent.text)
                    fetchSearchHistory()
                }
            }

            is TradeSearchIntent.Insert -> {
                launch {
                    val queryString = intent.text
                    if (queryString.isNotBlank()) {
                        insertSearchHistory(text = queryString)
                    }
                }
            }

            is TradeSearchIntent.RefreshSearchHistory -> {
                launch { fetchSearchHistory() }
            }
        }
    }
}
