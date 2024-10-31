package kr.linkerbell.campusmarket.android.data.remote.network.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper

@Module
@InstallIn(SingletonComponent::class)
object NetworkEnvironmentModule {

    @Provides
    @Singleton
    internal fun provideBaseUrlProvider(
        @ApplicationContext context: Context,
        dataStore: DataStore<Preferences>
    ): BaseUrlProvider {
        return BaseUrlProvider(context, dataStore)
    }

    @Provides
    @Singleton
    internal fun provideErrorMessageMapper(
        @ApplicationContext context: Context
    ): ErrorMessageMapper {
        return ErrorMessageMapper(context)
    }
}
