package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room

@Serializable
data class MakeRoomRes(
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
) : DataMapper<Room> {
    override fun toDomain(): Room {
        return Room(
            id = id,
            userId = userId,
            tradeId = itemId,
            readLatestMessageId = -1,
            title = title,
            isAlarm = isAlarm
        )
    }
}
