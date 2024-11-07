package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.search.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.CategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.GetCategoryListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.SearchTradeListUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import javax.inject.Inject

@HiltViewModel
class TradeSearchResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val searchTradeListUseCase: SearchTradeListUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<TradeSearchResultState> =
        MutableStateFlow(TradeSearchResultState.Init)
    val state: StateFlow<TradeSearchResultState> = _state.asStateFlow()

    private val _event: MutableEventFlow<TradeSearchResultEvent> = MutableEventFlow()
    val event: EventFlow<TradeSearchResultEvent> = _event.asEventFlow()

    private val _tradeList: MutableStateFlow<PagingData<Trade>> =
        MutableStateFlow(PagingData.empty())
    val tradeList: StateFlow<PagingData<Trade>> = _tradeList.asStateFlow()

    private val _categoryList: MutableStateFlow<List<String>> =
        MutableStateFlow(CategoryList.empty.categoryList)
    val categoryList: StateFlow<List<String>> = _categoryList.asStateFlow()

    private val _tradeSearchQuery: MutableStateFlow<TradeSearchQuery> =
        MutableStateFlow(TradeSearchQuery())
    val tradeSearchQuery: StateFlow<TradeSearchQuery> = _tradeSearchQuery.asStateFlow()

    init {
        _tradeSearchQuery.value = TradeSearchQuery(
            name = savedStateHandle["name"] ?: "",
            category = savedStateHandle["category"] ?: "",
            minPrice = savedStateHandle["minPrice"] ?: 0,
            maxPrice = savedStateHandle["maxPrice"] ?: Int.MAX_VALUE,
            sorted = savedStateHandle["sorted"] ?: ""
        )
        launch {
            getCategoryList()
            onUpdateQuery()
        }
    }

    fun onIntent(intent: TradeSearchResultIntent) {
        when (intent) {
            is TradeSearchResultIntent.ApplyNewQuery -> {
                _tradeSearchQuery.value = intent.newQuery
                launch {
                    getCategoryList()
                    onUpdateQuery()
                }
            }
        }
    }

    private suspend fun onUpdateQuery() {
        _state.value = TradeSearchResultState.Loading

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

    private suspend fun getCategoryList() {
        _state.value = TradeSearchResultState.Loading
        getCategoryListUseCase().onSuccess {
            _state.value = TradeSearchResultState.Init
            _categoryList.value = it.categoryList
        }.onFailure {
            _state.value = TradeSearchResultState.Init
            _categoryList.value = CategoryList.empty.categoryList
        }
    }
}
