package kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature.TokenApi
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.authentication.JwtToken
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository

class RealTokenRepository @Inject constructor(
    private val tokenApi: TokenApi,
    private val dataStore: DataStore<Preferences>
) : TokenRepository {

    private val _refreshFailEvent: MutableEventFlow<Unit> = MutableEventFlow()
    override val refreshFailEvent: EventFlow<Unit> = _refreshFailEvent.asEventFlow()

    override suspend fun login(
        idToken: String,
        firebaseToken: String
    ): Result<Unit> {
        return tokenApi.login(
            idToken = idToken,
            firebaseToken = firebaseToken,
        ).onSuccess { token ->
            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(REFRESH_TOKEN)] = token.refreshToken
                preferences[stringPreferencesKey(ACCESS_TOKEN)] = token.accessToken
            }
        }.map { }
    }

    override suspend fun getRefreshToken(): String {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(REFRESH_TOKEN)]
        }.first().orEmpty()
    }

    override suspend fun getAccessToken(): String {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(ACCESS_TOKEN)]
        }.first().orEmpty()
    }

    override suspend fun refreshToken(
        refreshToken: String
    ): Result<JwtToken> {
        return if (refreshToken.isEmpty()) {
            // TODO : 적절한 Exception 이름
            Result.failure(ServerException("Client Error", "refreshToken is empty."))
        } else {
            tokenApi.getAccessToken(
                refreshToken = refreshToken
            ).onSuccess { token ->
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(REFRESH_TOKEN)] = token.refreshToken
                    preferences[stringPreferencesKey(ACCESS_TOKEN)] = token.accessToken
                }
            }.onFailure { exception ->
                removeToken()
                _refreshFailEvent.emit(Unit)
            }.map { token ->
                JwtToken(
                    accessToken = token.accessToken,
                    refreshToken = token.refreshToken
                )
            }
        }
    }

    override suspend fun removeToken(): Result<Unit> {
        // TODO : KTOR-4759 BearerAuthProvider caches result of loadToken until process death
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(REFRESH_TOKEN))
            preferences.remove(stringPreferencesKey(ACCESS_TOKEN))
        }
        return Result.success(Unit)
    }

    companion object {
        private const val REFRESH_TOKEN = "refresh_token"
        private const val ACCESS_TOKEN = "access_token"
    }
}
