package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.inquiry

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class InquiryArgument(
    val state: InquiryState,
    val event: EventFlow<InquiryEvent>,
    val intent: (InquiryIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface InquiryState {
    data object Init : InquiryState
    data object Loading : InquiryState
}

sealed interface InquiryEvent {
    sealed interface PostInquiry : InquiryEvent {
        data object Success : PostInquiry
        data object Failure : PostInquiry
    }
}

sealed interface InquiryIntent {
    data class PostInquiry(
        val title: String,
        val category: String,
        val description: String
    ) : InquiryIntent
}
