package kr.linkerbell.campusmarket.android.data.remote.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.linkerbell.campusmarket.android.data.remote.local.database.sample.SampleDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.sample.SampleEntity
import kr.linkerbell.campusmarket.android.data.remote.local.database.sample.SearchHistoryDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.sample.SearchHistoryEntity

@Database(
    entities = [SampleEntity::class, SearchHistoryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CampusMarketDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        const val DATABASE_NAME = "campusmarket"
    }
}
