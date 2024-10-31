package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile

@Immutable
data class ChatRoomData(
    val roomList: List<Room>,
    val userProfileList: List<UserProfile>,
    val messageList: List<Message>
)
