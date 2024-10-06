package kr.linkerbell.campusmarket.android.domain.repository.nonfeature

import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.authentication.JwtToken

interface TokenRepository {

    val refreshFailEvent: EventFlow<Unit>

    // TODO : password encrypt
    suspend fun login(
        username: String,
        password: String
    ): Result<Long>

    suspend fun register(
        username: String,
        password: String
    ): Result<Long>

    suspend fun getRefreshToken(): String

    suspend fun getAccessToken(): String

    suspend fun refreshToken(
        refreshToken: String
    ): Result<JwtToken>

    suspend fun removeToken(): Result<Unit>
}
