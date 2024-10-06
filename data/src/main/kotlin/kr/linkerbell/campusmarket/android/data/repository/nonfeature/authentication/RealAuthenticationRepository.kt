package kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication

import kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature.AuthenticationApi
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository
import javax.inject.Inject

class RealAuthenticationRepository @Inject constructor(
    private val authenticationApi: AuthenticationApi,
    private val tokenRepository: TokenRepository
) : AuthenticationRepository {

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
