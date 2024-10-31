package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication.token

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository

class CheckRefreshTokenFilledUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Boolean {
        return tokenRepository.getRefreshToken().isNotEmpty()
    }
}
