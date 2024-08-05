package kr.linkerbell.boardlink.android.domain.usecase.nonfeature.authentication

import kr.linkerbell.boardlink.android.domain.repository.nonfeature.TokenRepository
import kr.linkerbell.boardlink.android.domain.usecase.nonfeature.tracking.SetTrackingProfileUseCase
import kr.linkerbell.boardlink.android.domain.usecase.nonfeature.user.GetProfileUseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val getProfileUseCase: GetProfileUseCase,
    private val setTrackingProfileUseCase: SetTrackingProfileUseCase
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): Result<Long> {
        return tokenRepository.login(
            username = username,
            password = password
        ).onSuccess {
            getProfileUseCase().onSuccess { profile ->
                setTrackingProfileUseCase(
                    profile = profile
                )
            }.getOrThrow()
        }
    }
}
