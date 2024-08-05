package kr.linkerbell.boardlink.android.domain.usecase.nonfeature.authentication.token

import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.boardlink.android.domain.repository.nonfeature.TokenRepository
import javax.inject.Inject

class GetTokenRefreshFailEventFlowUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(): EventFlow<Unit> {
        return tokenRepository.refreshFailEvent
    }
}
