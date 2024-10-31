package kr.linkerbell.campusmarket.android.data.repository.nonfeature.authentication

import javax.inject.Inject
import kotlinx.coroutines.delay
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository

class MockAuthenticationRepository @Inject constructor(
    private val tokenRepository: TokenRepository
) : AuthenticationRepository {

    override suspend fun sendEmailVerifyCode(
        email: String
    ): Result<String> {
        randomShortDelay()
        return Result.success("MOCK_VERIFY_CODE")
    }

    override suspend fun verifyEmailVerifyCode(
        token: String,
        verifyCode: String
    ): Result<Unit> {
        randomShortDelay()
        return Result.success(Unit)
    }

    override suspend fun logout(): Result<Unit> {
        randomShortDelay()
        return when {
            tokenRepository.getAccessToken().isEmpty()
                    || tokenRepository.getRefreshToken().isEmpty() -> {
                Result.failure(ServerException("MOCK_ERROR", "로그인 상태가 아닙니다."))
            }

            else -> {
                Result.success(Unit)
            }
        }.onSuccess {
            tokenRepository.removeToken()
        }
    }

    override suspend fun withdraw(): Result<Unit> {
        randomLongDelay()
        return when {
            tokenRepository.getAccessToken().isEmpty()
                    || tokenRepository.getRefreshToken().isEmpty() -> {
                Result.failure(ServerException("MOCK_ERROR", "로그인 상태가 아닙니다."))
            }

            else -> {
                Result.success(Unit)
            }
        }.onSuccess {
            tokenRepository.removeToken()
        }
    }

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
