package kr.linkerbell.boardlink.android.data.remote.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import kr.linkerbell.boardlink.android.data.remote.local.database.BoardlinkDatabase
import kr.linkerbell.boardlink.android.data.remote.local.database.sample.SampleDao
import kr.linkerbell.boardlink.android.data.remote.local.preferences.PreferencesConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    internal fun provideBoardlinkDatabase(
        @ApplicationContext context: Context
    ): BoardlinkDatabase {
        return Room.databaseBuilder(
            context,
            BoardlinkDatabase::class.java,
            BoardlinkDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    internal fun provideSampleDao(
        boardlinkDatabase: BoardlinkDatabase
    ): SampleDao {
        return boardlinkDatabase.sampleDao()
    }
}
