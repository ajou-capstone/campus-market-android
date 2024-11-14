package kr.linkerbell.campusmarket.android.data.remote.local.database.message

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg users: MessageEntity)

    @Query("SELECT * FROM message WHERE id = :id LIMIT 1")
    suspend fun get(id: Long): MessageEntity?

    // TODO : Paging 처리
    @Query("SELECT * FROM message WHERE room_id = :roomId")
    fun getByRoomId(roomId: Long): Flow<List<MessageEntity>>

    // TODO : Paging 처리
    @Query("SELECT * FROM message")
    fun getAll(): Flow<List<MessageEntity>>

    @Query("SELECT id FROM message WHERE id IN(:idList)")
    suspend fun findExistingIdList(idList: List<Long>): List<Long>

    @Update
    suspend fun update(vararg users: MessageEntity)

    @Delete
    suspend fun delete(vararg users: MessageEntity)
}
