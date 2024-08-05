package kr.linkerbell.boardlink.android.domain.usecase.nonfeature.authentication

import kr.linkerbell.boardlink.android.domain.repository.nonfeature.AuthenticationRepository
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authenticationRepository.withdraw()
    }
}
