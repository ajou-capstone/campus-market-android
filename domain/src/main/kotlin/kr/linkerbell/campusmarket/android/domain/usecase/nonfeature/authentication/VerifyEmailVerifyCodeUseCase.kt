package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository

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
