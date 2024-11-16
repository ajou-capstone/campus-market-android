package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile

@Immutable
data class ChatData(
    val messageList: List<Message>,
    val userProfile: UserProfile,
    val myProfile: MyProfile,
    val room: Room,
    val trade: TradeInfo
)
