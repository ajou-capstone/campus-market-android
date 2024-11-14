package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message

@Serializable
@JsonClassDiscriminator("contentType")
@OptIn(ExperimentalSerializationApi::class)
sealed interface MessageRes : DataMapper<Message> {

    @Serializable
    @SerialName("TEXT")
    data class Text(
        @SerialName("messageId")
        val id: Long,
        @SerialName("chatRoomId")
        val chatRoomId: Long,
        @SerialName("userId")
        val userId: Long,
        @SerialName("content")
        val content: String,
        @SerialName("createdAt")
        val createdAt: LocalDateTime
    ) : MessageRes {
        override fun toDomain(): Message.Text {
            return Message.Text(
                id = id,
                chatRoomId = chatRoomId,
                userId = userId,
                content = content,
                createdAt = createdAt.toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
            )
        }
    }

    @Serializable
    @SerialName("IMAGE")
    data class Image(
        @SerialName("messageId")
        val id: Long,
        @SerialName("chatRoomId")
        val chatRoomId: Long,
        @SerialName("userId")
        val userId: Long,
        @SerialName("content")
        val content: String,
        @SerialName("createdAt")
        val createdAt: LocalDateTime
    ) : MessageRes {
        override fun toDomain(): Message.Image {
            return Message.Image(
                id = id,
                chatRoomId = chatRoomId,
                userId = userId,
                content = content,
                createdAt = createdAt.toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
            )
        }
    }

    @Serializable
    @SerialName("TIMETABLE")
    data class Schedule(
        @SerialName("messageId")
        val id: Long,
        @SerialName("chatRoomId")
        val chatRoomId: Long,
        @SerialName("userId")
        val userId: Long,
        @SerialName("createdAt")
        val createdAt: LocalDateTime
    ) : MessageRes {
        override fun toDomain(): Message.Schedule {
            return Message.Schedule(
                id = id,
                chatRoomId = chatRoomId,
                userId = userId,
                createdAt = createdAt.toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
            )
        }
    }
}
