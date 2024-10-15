package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication

import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository
import javax.inject.Inject

class VerifyEmailVerifyCodeUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(
        token: String,
        verifyCode: String
    ): Result<Unit> {
        return authenticationRepository.verifyEmailVerifyCode(
            token = token,
            verifyCode = verifyCode
        )
    }
}
