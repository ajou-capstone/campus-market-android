package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel

@HiltViewModel
class InquiryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val _state: MutableStateFlow<InquiryState> = MutableStateFlow(InquiryState.Init)
    val state: StateFlow<InquiryState> = _state.asStateFlow()

    private val _event: MutableEventFlow<InquiryEvent> = MutableEventFlow()
    val event: EventFlow<InquiryEvent> = _event.asEventFlow()

    fun onIntent(intent: InquiryIntent) {

    }
}
