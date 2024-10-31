package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room

@Serializable
data class GetRoomListRes(
    @SerialName("data")
    val data: List<GetRoomListItemRes>
) : DataMapper<List<Room>> {
    override fun toDomain(): List<Room> {
        return data.map { it.toDomain() }
    }
}

@Serializable
data class GetRoomListItemRes(
    @SerialName("chatRoomId")
    val id: Long,
    @SerialName("userId")
    val userId: Long,
    @SerialName("itemId")
    val itemId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("isAlarm")
    val isAlarm: Boolean,
    @SerialName("messageId")
    val readLatestMessageId: Long
) : DataMapper<Room> {
    override fun toDomain(): Room {
        return Room(
            id = id,
            userId = userId,
            tradeId = itemId,
            readLatestMessageId = readLatestMessageId,
            title = title,
            isAlarm = isAlarm
        )
    }
}
