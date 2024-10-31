package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow

@Immutable
data class ChatRoomArgument(
    val state: ChatRoomState,
    val event: EventFlow<ChatRoomEvent>,
    val intent: (ChatRoomIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ChatRoomState {
    data object Init : ChatRoomState
}

sealed interface ChatRoomEvent

sealed interface ChatRoomIntent {
    data class SetRoomNotification(val id: Long, val isNotification: Boolean) : ChatRoomIntent
}
