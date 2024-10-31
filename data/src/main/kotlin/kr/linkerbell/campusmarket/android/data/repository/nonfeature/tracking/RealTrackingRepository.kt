package kr.linkerbell.campusmarket.android.data.repository.nonfeature.tracking

import androidx.annotation.Size
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.setCustomKeys
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository

class RealTrackingRepository @Inject constructor(
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
        return runCatching {
            Firebase.analytics.run {
                setUserId(myProfile.id.toString())
                setUserProperty("name", myProfile.nickname)
                setUserProperty("nickname", myProfile.nickname)
                setUserProperty("email", myProfile.loginEmail)
            }
            Firebase.crashlytics.run {
                setUserId(myProfile.id.toString())
                setCustomKeys {
                    key("name", myProfile.nickname)
                    key("nickname", myProfile.nickname)
                    key("email", myProfile.loginEmail)
                }
            }
        }
    }

    override suspend fun logEvent(
        @Size(min = 1, max = 40) eventName: String,
        params: Map<String, Any>
    ): Result<Unit> {
        return runCatching {
            Firebase.analytics.logEvent(
                eventName,
                bundleOf(
                    *params.map { (key, value) ->
                        when (value) {
                            is String -> key to value
                            is Int -> key to value.toLong()
                            is Long -> key to value
                            is Float -> key to value.toDouble()
                            is Double -> key to value
                            is Boolean -> key to value.toString()
                            else -> throw IllegalArgumentException("Invalid value type")
                        }
                    }.toTypedArray()
                )
            )
        }
    }

    companion object {
        private const val FCM_TOKEN = "fcm_token"
    }
}
