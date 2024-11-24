package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.info

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class InquiryInfoArgument(
    val state: InquiryInfoState,
    val event: EventFlow<InquiryInfoEvent>,
    val intent: (InquiryInfoIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface InquiryInfoState {
    data object Init : InquiryInfoState
    data object Loading : InquiryInfoState
}

sealed interface InquiryInfoEvent

sealed interface InquiryInfoIntent
