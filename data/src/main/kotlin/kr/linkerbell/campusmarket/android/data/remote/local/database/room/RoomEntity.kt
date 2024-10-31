package kr.linkerbell.campusmarket.android.data.remote.local.database.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room

@Entity(tableName = "room")
data class RoomEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "trade_id") val tradeId: Long,
    @ColumnInfo(name = "read_latest_message_id") val readLatestMessageId: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "is_alarm") val isAlarm: Boolean
) : DataMapper<Room> {
    override fun toDomain(): Room {
        return Room(
            id = id,
            userId = userId,
            tradeId = tradeId,
            readLatestMessageId = readLatestMessageId,
            title = title,
            isAlarm = isAlarm
        )
    }
}

fun Room.toEntity(): RoomEntity {
    return RoomEntity(
        id = id,
        userId = userId,
        tradeId = tradeId,
        readLatestMessageId = readLatestMessageId,
        title = title,
        isAlarm = isAlarm
    )
}
