package kr.linkerbell.campusmarket.android.data.remote.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kr.linkerbell.campusmarket.android.data.remote.local.database.CampusMarketDatabase
import kr.linkerbell.campusmarket.android.data.remote.local.database.message.MessageDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.room.RoomDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.searchhistory.SearchHistoryDao
import kr.linkerbell.campusmarket.android.data.remote.local.preferences.PreferencesConstant

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PreferencesConstant.PREFERENCES_NAME
    )

    @Provides
    @Singleton
    internal fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    internal fun provideCampusMarketDatabase(
        @ApplicationContext context: Context
    ): CampusMarketDatabase {
        return Room.databaseBuilder(
            context,
            CampusMarketDatabase::class.java,
            CampusMarketDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    internal fun provideMessageDao(
        campusMarketDatabase: CampusMarketDatabase
    ): MessageDao {
        return campusMarketDatabase.messageDao()
    }

    @Provides
    @Singleton
    internal fun provideRoomDao(
        campusMarketDatabase: CampusMarketDatabase
    ): RoomDao {
        return campusMarketDatabase.roomDao()
    }

    @Provides
    @Singleton
    internal fun provideSearchHistoryDao(
        campusMarketDatabase: CampusMarketDatabase
    ): SearchHistoryDao {
        return campusMarketDatabase.searchHistoryDao()
    }
}
