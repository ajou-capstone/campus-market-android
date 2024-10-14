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
                    userId = 1L,
                    campusId = 1L,
                    loginEmail = "lorenzo.ballard@example.com",
                    schoolEmail = "selena.weaver@example.com",
                    nickname = "장성혁",
                    profileImage = "https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50",
                    rating = 4.5

                )
            )
        } else {
            Result.failure(
                ServerException("MOCK_ERROR", "로그인이 필요합니다.")
            )
        }
    }

    override suspend fun setProfile(
        nickname: String,
        profileImage: String
    ): Result<Unit> {
        return Result.success(Unit)
    }

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
