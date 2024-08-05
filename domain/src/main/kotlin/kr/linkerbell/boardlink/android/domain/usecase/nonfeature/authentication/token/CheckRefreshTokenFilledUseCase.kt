package kr.linkerbell.boardlink.android.domain.usecase.nonfeature.authentication.token

import kr.linkerbell.boardlink.android.domain.repository.nonfeature.TokenRepository
import javax.inject.Inject

class CheckRefreshTokenFilledUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(): Boolean {
        return tokenRepository.getRefreshToken().isNotEmpty()
    }
}
