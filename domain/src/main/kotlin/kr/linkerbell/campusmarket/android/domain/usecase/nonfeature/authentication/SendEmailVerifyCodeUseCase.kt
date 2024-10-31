package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository

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
