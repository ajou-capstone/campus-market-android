package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication.token

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TokenRepository

class GetTokenRefreshFailEventFlowUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(): EventFlow<Unit> {
        return tokenRepository.refreshFailEvent
    }
}
