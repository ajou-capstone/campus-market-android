package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.view

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class InquiryViewArgument(
    val state: InquiryViewState,
    val event: EventFlow<InquiryViewEvent>,
    val intent: (InquiryViewIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface InquiryViewState {
    data object Init : InquiryViewState
    data object Loading : InquiryViewState
}

sealed interface InquiryViewEvent

sealed interface InquiryViewIntent
