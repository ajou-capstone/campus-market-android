package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tracking.SetTrackingProfileUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetFcmTokenUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase

class LoginUseCase @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getFcmTokenUseCase: GetFcmTokenUseCase,
    private val setTrackingProfileUseCase: SetTrackingProfileUseCase
) {
    suspend operator fun invoke(
        idToken: String
    ): Result<Unit> {
        return tokenRepository.login(
            idToken = idToken,
            firebaseToken = getFcmTokenUseCase()
        ).onSuccess {
            getMyProfileUseCase().onSuccess { profile ->
                setTrackingProfileUseCase(
                    myProfile = profile
                )
            }.getOrThrow()
        }
    }
}
