package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry

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

sealed interface InquiryEvent

sealed interface InquiryIntent
