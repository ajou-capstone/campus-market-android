package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_review

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class MyRecentReviewArgument(
    val state: MyRecentReviewState,
    val event: EventFlow<MyRecentReviewEvent>,
    val intent: (MyRecentReviewIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface MyRecentReviewState {
    data object Init : MyRecentReviewState
    data object Loading : MyRecentReviewState
}

sealed interface MyRecentReviewEvent

sealed interface MyRecentReviewIntent
