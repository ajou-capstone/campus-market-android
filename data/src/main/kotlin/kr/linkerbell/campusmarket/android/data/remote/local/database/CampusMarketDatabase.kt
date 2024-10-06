package kr.linkerbell.campusmarket.android.data.remote.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.linkerbell.campusmarket.android.data.remote.local.database.sample.SampleDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.sample.SampleEntity

@Database(entities = [SampleEntity::class], version = 1, exportSchema = false)
abstract class CampusMarketDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao

    companion object {
        const val DATABASE_NAME = "campusmarket"
    }
}
