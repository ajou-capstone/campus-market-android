package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.notification

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class NotificationArgument(
    val state: NotificationState,
    val event: EventFlow<NotificationEvent>,
    val intent: (NotificationIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface NotificationState {
    data object Init : NotificationState
    data object Loading : NotificationState
}

sealed interface NotificationEvent

sealed interface NotificationIntent {
    data object RefreshData : NotificationIntent
    data class DeleteNotificationById(val notificationId: Long) : NotificationIntent
    data object DeleteAllNotification : NotificationIntent
}
