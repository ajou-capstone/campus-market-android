package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room

@Serializable
data class MakeRoomRes(
    @SerialName("chatRoom")
    val chatRoom: MakeRoomRoomRes,
    @SerialName("messageId")
    val readLatestMessageId: Long
) : DataMapper<Room> {
    override fun toDomain(): Room {
        return Room(
            id = chatRoom.id,
            userId = chatRoom.userId,
            tradeId = chatRoom.itemId,
            readLatestMessageId = readLatestMessageId,
            title = chatRoom.title,
            isAlarm = chatRoom.isAlarm
        )
    }
}

@Serializable
data class MakeRoomRoomRes(
    @SerialName("chatRoomId")
    val id: Long,
    @SerialName("userId")
    val userId: Long,
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("isAlarm")
    val isAlarm: Boolean
)
