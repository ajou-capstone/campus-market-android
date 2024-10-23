package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message

@Serializable
@SerialName("TEXT")
data class GetMessageListByIdRes(
    @SerialName("data")
    val data: List<GetMessageListByIdItemRes>
) : DataMapper<List<Message>> {
    override fun toDomain(): List<Message> {
        return data.map { it.toDomain() }
    }
}

@Serializable
@JsonClassDiscriminator("contentType")
@OptIn(ExperimentalSerializationApi::class)
sealed class GetMessageListByIdItemRes : DataMapper<Message> {

    @Serializable
    @SerialName("TEXT")
    data class GetMessageListByIdItemTextRes(
        @SerialName("messageId")
        val id: Long,
        @SerialName("chatRoomId")
        val chatRoomId: Long,
        @SerialName("userId")
        val userId: Long,
        @SerialName("content")
        val content: String,
        @SerialName("createdAt")
        val createdAt: Long
    ) : GetMessageListByIdItemRes() {
        override fun toDomain(): Message.Text {
            return Message.Text(
                id = id,
                chatRoomId = chatRoomId,
                userId = userId,
                content = content,
                createdAt = createdAt
            )
        }
    }

    @Serializable
    @SerialName("IMAGE")
    data class GetMessageListByIdItemImageRes(
        @SerialName("messageId")
        val id: Long,
        @SerialName("chatRoomId")
        val chatRoomId: Long,
        @SerialName("userId")
        val userId: Long,
        @SerialName("content")
        val content: String,
        @SerialName("createdAt")
        val createdAt: Long
    ) : GetMessageListByIdItemRes() {
        override fun toDomain(): Message.Image {
            return Message.Image(
                id = id,
                chatRoomId = chatRoomId,
                userId = userId,
                content = content,
                createdAt = createdAt
            )
        }
    }
}
