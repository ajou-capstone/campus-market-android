package kr.linkerbell.campusmarket.android.data.remote.local.database.searchhistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg queryString: SearchHistoryEntity)

    @Query("SELECT * FROM search_history")
    suspend fun getAll(): List<SearchHistoryEntity>

    @Query("DELETE FROM search_history WHERE query_string = :text")
    suspend fun deleteByText(text: String)

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()
}
