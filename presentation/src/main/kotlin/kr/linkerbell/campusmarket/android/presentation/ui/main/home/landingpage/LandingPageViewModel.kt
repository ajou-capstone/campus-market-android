package kr.linkerbell.campusmarket.android.presentation.ui.main.home.landingpage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.ItemQueryParameter
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.SummarizedItem
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.item.GetSummarizedLatestItemListUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSummarizedLatestItemsUseCase: GetSummarizedLatestItemListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<LandingPageState> = MutableStateFlow(LandingPageState.Init)
    val state: StateFlow<LandingPageState> = _state.asStateFlow()

    private val _event: MutableEventFlow<LandingPageEvent> = MutableEventFlow()
    val event: EventFlow<LandingPageEvent> = _event.asEventFlow()

    private val _latestItemsList: MutableStateFlow<MutableList<SummarizedItem>> =
        MutableStateFlow(mutableListOf())
    val latestItemsList: StateFlow<MutableList<SummarizedItem>> = _latestItemsList.asStateFlow()

    private var currentPage = 0

    init {
        launch {
            _state.value = LandingPageState.Loading
            getSummarizedLatestItemsUseCase.getSummarizedLatestItemsUseCase(ItemQueryParameter.empty)
                .onSuccess {
                    _state.value = LandingPageState.Init
                    _latestItemsList.value = it
                }.onFailure { exception ->
                    _state.value = LandingPageState.Init
                    when (exception) {
                        is ServerException -> {
                            _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                        }

                        else -> {
                            _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                        }
                    }
                }
        }
    }


    fun onIntent(intent: LandingPageIntent) {

    }

    fun loadMoreItems() {
        viewModelScope.launch {

            _state.value = LandingPageState.Loading
            val query = ItemQueryParameter.empty
            val newItems = getSummarizedLatestItemsUseCase.getSummarizedLatestItemsUseCase(
                query.copy(
                    pageNum = currentPage,
                    pageSize = 7
                )
            )
            newItems.onSuccess {
                _state.value = LandingPageState.Init
                val updatedList = _latestItemsList.value + it
                _latestItemsList.value = updatedList.toMutableList()
                currentPage++
            }.onFailure { exception ->
                _state.value = LandingPageState.Init
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }

        }
    }
}
