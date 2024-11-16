package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat

import androidx.compose.runtime.Immutable
import kotlin.coroutines.CoroutineContext
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.ChatRoomIntent.Session

@Immutable
data class ChatArgument(
    val state: ChatState,
    val event: EventFlow<ChatEvent>,
    val intent: (ChatIntent) -> Unit,
    val logEvent: (eventName: String, params: Map<String, Any>) -> Unit,
    val coroutineContext: CoroutineContext
)

sealed interface ChatState {
    data object Init : ChatState
}

sealed interface ChatEvent {
    sealed interface Sell : ChatEvent {
        data object Success : Sell
    }
}

sealed interface ChatIntent {
    data object Refresh : ChatIntent

    sealed interface Session : ChatIntent {
        data object Connect : Session
        data object Subscribe : Session
        data class SendText(val text: String) : Session
        data class SendImage(val image: GalleryImage) : Session
        data object SendSchedule : Session
        data object Disconnect : Session
    }

    data object OnSell : ChatIntent
}
