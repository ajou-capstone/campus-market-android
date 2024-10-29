package kr.linkerbell.campusmarket.android.data.remote.local.database.sample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchHistory")
data class SearchHistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "queryString") val queryString: String
)
