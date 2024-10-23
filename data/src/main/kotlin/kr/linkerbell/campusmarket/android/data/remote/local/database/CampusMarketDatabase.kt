package kr.linkerbell.campusmarket.android.data.remote.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.linkerbell.campusmarket.android.data.remote.local.database.message.MessageDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.room.RoomDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.room.RoomEntity

@Database(entities = [RoomEntity::class], version = 1, exportSchema = false)
abstract class CampusMarketDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDao

    abstract fun messageDao(): MessageDao

    companion object {
        const val DATABASE_NAME = "campusmarket"
    }
}
