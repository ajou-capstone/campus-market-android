package kr.linkerbell.campusmarket.android.presentation.ui.main.home.landingpage

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
class LandingPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val searchTradeListUseCase: SearchTradeListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<LandingPageState> = MutableStateFlow(LandingPageState.Init)
    val state: StateFlow<LandingPageState> = _state.asStateFlow()

    private val _event: MutableEventFlow<LandingPageEvent> = MutableEventFlow()
    val event: EventFlow<LandingPageEvent> = _event.asEventFlow()

    private val _tradeList: MutableStateFlow<PagingData<Trade>> =
        MutableStateFlow(PagingData.empty())
    val tradeList: StateFlow<PagingData<Trade>> = _tradeList.asStateFlow()

    init {
        launch {
            searchTradeListUseCase(
                name = "",
                category = "",
                minPrice = 0,
                maxPrice = 0,
                sorted = ""
            )
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
                    _tradeList.value = it
                }
        }
    }

    fun onIntent(intent: LandingPageIntent) {

    }
}
