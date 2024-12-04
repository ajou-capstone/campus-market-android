package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.view.list

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
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.SummarizedUserReport
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report.GetInquiryListUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getInquiryListUseCase: GetInquiryListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ReportListState> = MutableStateFlow(ReportListState.Init)
    val state: StateFlow<ReportListState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ReportListEvent> = MutableEventFlow()
    val event: EventFlow<ReportListEvent> = _event.asEventFlow()

    private val _inquiryList: MutableStateFlow<PagingData<SummarizedUserReport>> =
        MutableStateFlow(PagingData.empty())
    val inquiryList: StateFlow<PagingData<SummarizedUserReport>> = _inquiryList.asStateFlow()

    fun onIntent(intent: ReportListIntent) {
        when(intent){
            is ReportListIntent.RefreshData -> {
                launch{
                    getInquiryList()
                }
            }
        }
    }

    init {
        launch {
            getInquiryList()
        }
    }

    private suspend fun getInquiryList() {
        getInquiryListUseCase()
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
                _inquiryList.value = it
            }
    }
}
