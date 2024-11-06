package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat

import kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.ChatRoomConstant

object ChatConstant {
    const val ROUTE = ChatRoomConstant.ROUTE
    const val ROUTE_ARGUMENT_ROOM_ID = "room_id"
    const val ROUTE_STRUCTURE = "${ChatRoomConstant.ROUTE}/{$ROUTE_ARGUMENT_ROOM_ID}"
}
