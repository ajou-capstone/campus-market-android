package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.history.DeleteAllSearchHistoryUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.history.DeleteSearchHistoryByTextUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.history.GetSearchHistoryListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.history.InsertSearchHistoryUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel

@HiltViewModel
class TradeSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSearchHistoryListUseCase: GetSearchHistoryListUseCase,
    private val deleteSearchHistoryByTextUseCase: DeleteSearchHistoryByTextUseCase,
    private val deleteAllSearchHistoryUseCase: DeleteAllSearchHistoryUseCase,
    private val insertSearchHistoryUseCase: InsertSearchHistoryUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<TradeSearchState> = MutableStateFlow(TradeSearchState.Init)
    val state: StateFlow<TradeSearchState> = _state.asStateFlow()

    private val _event: MutableEventFlow<TradeSearchEvent> = MutableEventFlow()
    val event: EventFlow<TradeSearchEvent> = _event.asEventFlow()

    private val _searchHistory: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()

    init {
        launch {
            getSearchHistoryListUseCase()
                .catch {
                    _searchHistory.value = emptyList()
                }.collect {
                    _searchHistory.value = it
                }
        }
    }

    fun onIntent(intent: TradeSearchIntent) {
        when (intent) {
            is TradeSearchIntent.DeleteAll -> {
                launch {
                    deleteAllSearchHistory()
                }
            }

            is TradeSearchIntent.DeleteByText -> {
                launch {
                    deleteSearchHistory(text = intent.text)
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
}
