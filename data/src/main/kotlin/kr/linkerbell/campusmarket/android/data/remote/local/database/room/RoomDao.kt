package kr.linkerbell.campusmarket.android.data.remote.local.database.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg users: RoomEntity)

    @Query("SELECT * FROM room WHERE id = :id LIMIT 1")
    fun get(id: Long): Flow<RoomEntity>

    @Query("SELECT * FROM room")
    fun getAll(): Flow<List<RoomEntity>>

    @Update
    suspend fun update(vararg users: RoomEntity)

    @Query("DELETE FROM room WHERE id = :id")
    suspend fun delete(id: Long)
}
