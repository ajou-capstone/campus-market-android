package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.tradesearchpage

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

    fun onIntent(intent: TradeSearchIntent) {
        when (intent) {
            is TradeSearchIntent.DeleteAll -> {
                deleteAllSearchHistory()
            }

            is TradeSearchIntent.DeleteByText -> {
                deleteSearchHistory(text = intent.text)
            }

            is TradeSearchIntent.Insert -> {
                insertSearchHistory(text = intent.text)
            }
        }
    }

    private fun deleteSearchHistory(text: String) {
        launch {
            deleteSearchHistoryByTextUseCase(text)
        }
    }

    private fun deleteAllSearchHistory() {
        launch {
            deleteAllSearchHistoryUseCase()
        }
    }

    private fun insertSearchHistory(text: String){
        launch{
            insertSearchHistoryUseCase(text)
        }
    }
}
