package kr.linkerbell.campusmarket.android.data.remote.local.database.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg users: UserProfileEntity)

    @Query("SELECT * FROM user WHERE id = :id LIMIT 1")
    fun get(id: Long): Flow<UserProfileEntity>

    @Update
    suspend fun update(vararg users: UserProfileEntity)

    @Delete
    suspend fun delete(vararg users: UserProfileEntity)
}
