package kr.linkerbell.campusmarket.android.data.repository.nonfeature.user

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Profile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.delay

class MockUserRepository @Inject constructor(
    private val tokenRepository: TokenRepository
) : UserRepository {

    override suspend fun getProfile(): Result<Profile> {
        randomShortDelay()
        val isLogined = tokenRepository.getAccessToken().isNotEmpty()
        return if (isLogined) {
            Result.success(
                Profile(
                    id = 1,
                    name = "장성혁",
                    nickname = "Ray Jang",
                    email = "ajou4095@gmail.com"
                )
            )
        } else {
            Result.failure(
                ServerException("MOCK_ERROR", "로그인이 필요합니다.")
            )
        }
    }

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
