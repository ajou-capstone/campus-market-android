package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.AuthenticationRepository

class LogoutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authenticationRepository.logout()
    }
}
