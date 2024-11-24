package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.post

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class InquiryPostArgument(
    val state: InquiryPostState,
    val event: EventFlow<InquiryPostEvent>,
    val intent: (InquiryPostIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface InquiryPostState {
    data object Init : InquiryPostState
    data object Loading : InquiryPostState
}

sealed interface InquiryPostEvent {
    sealed interface PostInquiry : InquiryPostEvent {
        data object Success : PostInquiry
        data object Failure : PostInquiry
    }
}

sealed interface InquiryPostIntent{
    data class PostInquiry(
        val title: String,
        val category: String,
        val description: String
    ) : InquiryPostIntent
}
