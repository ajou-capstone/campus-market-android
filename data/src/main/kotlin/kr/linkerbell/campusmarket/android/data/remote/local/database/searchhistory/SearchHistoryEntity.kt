package kr.linkerbell.campusmarket.android.data.remote.local.database.searchhistory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "query_string") val queryString: String
) : DataMapper<String> {
    override fun toDomain(): String {
        return queryString
    }
}
