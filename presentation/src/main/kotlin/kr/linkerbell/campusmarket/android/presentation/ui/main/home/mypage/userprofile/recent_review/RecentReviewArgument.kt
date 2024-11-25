package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_review

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class RecentReviewArgument(
    val state: RecentReviewState,
    val event: EventFlow<RecentReviewEvent>,
    val intent: (RecentReviewIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface RecentReviewState {
    data object Init : RecentReviewState
    data object Loading : RecentReviewState
}

sealed interface RecentReviewEvent

sealed interface RecentReviewIntent
