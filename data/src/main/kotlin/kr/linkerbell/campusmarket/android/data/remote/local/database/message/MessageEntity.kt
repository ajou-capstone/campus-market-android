package kr.linkerbell.campusmarket.android.data.remote.local.database.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message

private const val CONTENT_TYPE_TEXT = "TEXT"
private const val CONTENT_TYPE_IMAGE = "IMAGE"
private const val CONTENT_TYPE_SCHEDULE = "TIMETABLE"

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "room_id") val roomId: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "content_type") val contentType: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "created_at") val createdAt: Long
) : DataMapper<Message> {
    override fun toDomain(): Message {
        return when (contentType) {
            CONTENT_TYPE_TEXT -> Message.Text(
                id = id,
                chatRoomId = roomId,
                userId = userId,
                content = content,
                createdAt = createdAt
            )

            CONTENT_TYPE_IMAGE -> Message.Image(
                id = id,
                chatRoomId = roomId,
                userId = userId,
                content = content,
                createdAt = createdAt
            )

            CONTENT_TYPE_SCHEDULE -> Message.Schedule(
                id = id,
                chatRoomId = roomId,
                userId = userId,
                createdAt = createdAt
            )

            else -> throw IllegalArgumentException("Unknown content type: $contentType")
        }
    }
}

fun Message.toEntity(): MessageEntity {
    return when (this) {
        is Message.Text -> MessageEntity(
            id = id,
            roomId = chatRoomId,
            userId = userId,
            contentType = CONTENT_TYPE_TEXT,
            content = content,
            createdAt = createdAt
        )

        is Message.Image -> MessageEntity(
            id = id,
            roomId = chatRoomId,
            userId = userId,
            contentType = CONTENT_TYPE_IMAGE,
            content = content,
            createdAt = createdAt
        )

        is Message.Schedule -> MessageEntity(
            id = id,
            roomId = chatRoomId,
            userId = userId,
            contentType = CONTENT_TYPE_SCHEDULE,
            content = "",
            createdAt = createdAt
        )
    }
}
