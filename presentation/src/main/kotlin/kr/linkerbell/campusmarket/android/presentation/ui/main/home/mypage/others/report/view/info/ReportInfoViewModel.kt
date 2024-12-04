package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.view.info

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.ReportInfo
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.report.GetInquiryInfoUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class ReportInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getInquiryInfoUseCase: GetInquiryInfoUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ReportInfoState> = MutableStateFlow(ReportInfoState.Init)
    val state: StateFlow<ReportInfoState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ReportInfoEvent> = MutableEventFlow()
    val event: EventFlow<ReportInfoEvent> = _event.asEventFlow()

    private val _reportInfo: MutableStateFlow<ReportInfo> = MutableStateFlow(ReportInfo.empty)
    val reportInfo: StateFlow<ReportInfo> = _reportInfo.asStateFlow()

    private val _qaId: MutableStateFlow<Long> = MutableStateFlow(-1)

    fun onIntent(intent: ReportInfoIntent) {

    }

    init {
        launch {
            _qaId.value =
                savedStateHandle.get<Long>(ReportInfoConstant.ROUTE_ARGUMENT_QA_ID) ?: -1L
            getInquiryInfo(_qaId.value)
        }
    }

    private suspend fun getInquiryInfo(qaId: Long) {
        getInquiryInfoUseCase(qaId).onSuccess {
            _reportInfo.value = it
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _reportInfo.value = ReportInfo.empty
        }
    }
}
