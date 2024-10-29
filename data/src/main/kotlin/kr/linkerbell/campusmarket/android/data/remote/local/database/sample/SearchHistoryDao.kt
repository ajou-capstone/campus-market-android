package kr.linkerbell.campusmarket.android.data.remote.local.database.sample

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg queryString: SearchHistoryEntity)

    @Query("SELECT * FROM searchHistory")
    suspend fun getAll(): List<SearchHistoryEntity>

    @Query("DELETE FROM searchHistory WHERE queryString = :text")
    suspend fun deleteByText(text: String)

    @Query("DELETE FROM searchHistory")
    suspend fun deleteAll()
}
