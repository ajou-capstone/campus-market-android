package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication

import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository
import javax.inject.Inject

class SendEmailVerifyCodeUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(
        email: String
    ): Result<String> {
        return authenticationRepository.sendEmailVerifyCode(
            email = email
        )
    }
}
