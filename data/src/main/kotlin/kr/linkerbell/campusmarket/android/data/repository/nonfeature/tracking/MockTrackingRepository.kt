package kr.linkerbell.campusmarket.android.data.repository.nonfeature.tracking

import androidx.annotation.Size
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository

class MockTrackingRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : TrackingRepository {

    override suspend fun setFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(FCM_TOKEN)] = token
        }
    }

    override suspend fun getFcmToken(): String {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(FCM_TOKEN)]
        }.first().orEmpty()
    }

    override suspend fun setProfile(
        myProfile: MyProfile
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun logEvent(
        @Size(min = 1, max = 40) eventName: String,
        params: Map<String, Any>
    ): Result<Unit> {
        return if (
            params.values.any {
                it !is String && it !is Int && it !is Long && it !is Float && it !is Double && it !is Boolean
            }
        ) {
            Result.failure(IllegalArgumentException("Invalid value type"))
        } else {
            Result.success(Unit)
        }
    }

    companion object {
        private const val FCM_TOKEN = "fcm_token"
    }
}
