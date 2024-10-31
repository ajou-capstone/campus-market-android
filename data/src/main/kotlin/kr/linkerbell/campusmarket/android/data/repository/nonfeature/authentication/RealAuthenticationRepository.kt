package kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature.AuthenticationApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository

class RealAuthenticationRepository @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val tokenRepository: TokenRepository
) : AuthenticationRepository {

    override suspend fun sendEmailVerifyCode(
        email: String
    ): Result<String> {
        return authenticationApi.sendEmailVerifyCode(
            email = email
        ).toDomain()
    }

    override suspend fun verifyEmailVerifyCode(
        token: String,
        verifyCode: String
    ): Result<Unit> {
        return authenticationApi.verifyEmailVerifyCode(
            token = token,
            verifyCode = verifyCode
        )
    }

    override suspend fun logout(): Result<Unit> {
        return authenticationApi.logout()
            .onSuccess {
                tokenRepository.removeToken()
            }
    }

    override suspend fun withdraw(): Result<Unit> {
        return authenticationApi.withdraw()
            .onSuccess {
                tokenRepository.removeToken()
            }
    }
}
