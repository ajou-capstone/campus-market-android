package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.user

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class UserReportArgument(
    val state: UserReportState,
    val event: EventFlow<UserReportEvent>,
    val intent: (UserReportIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface UserReportState {
    data object Init : UserReportState
    data object Loading : UserReportState
}

sealed interface UserReportEvent {
    sealed interface PostInquiry : UserReportEvent {
        data object Success : PostInquiry
        data object Failure : PostInquiry
    }
}

sealed interface UserReportIntent {
    data class PostUserReport(
        val category: String,
        val description: String
    ) : UserReportIntent
}
