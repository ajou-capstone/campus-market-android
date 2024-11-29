package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.info

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.InquiryInfo
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.GetInquiryInfoUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class InquiryInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getInquiryInfoUseCase: GetInquiryInfoUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<InquiryInfoState> = MutableStateFlow(InquiryInfoState.Init)
    val state: StateFlow<InquiryInfoState> = _state.asStateFlow()

    private val _event: MutableEventFlow<InquiryInfoEvent> = MutableEventFlow()
    val event: EventFlow<InquiryInfoEvent> = _event.asEventFlow()

    private val _inquiryInfo: MutableStateFlow<InquiryInfo> = MutableStateFlow(InquiryInfo.empty)
    val inquiryInfo: StateFlow<InquiryInfo> = _inquiryInfo.asStateFlow()

    private val _qaId: MutableStateFlow<Long> = MutableStateFlow(-1)

    fun onIntent(intent: InquiryInfoIntent) {

    }

    init {
        launch {
            _qaId.value =
                savedStateHandle.get<Long>(InquiryInfoConstant.ROUTE_ARGUMENT_QA_ID) ?: -1L
            getInquiryInfo(_qaId.value)
        }
    }

    private suspend fun getInquiryInfo(qaId: Long) {
        getInquiryInfoUseCase(qaId).onSuccess {
            _inquiryInfo.value = it
        }.onFailure { exception ->
            when (exception) {
                is ServerException -> {
                    _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                }

                else -> {
                    _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                }
            }
            _inquiryInfo.value = InquiryInfo.empty
        }
    }
}
