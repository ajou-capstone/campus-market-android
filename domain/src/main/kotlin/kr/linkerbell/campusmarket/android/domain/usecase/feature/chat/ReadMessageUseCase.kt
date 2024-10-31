package kr.linkerbell.campusmarket.android.domain.usecase.feature.chat

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.ChatRepository

class ReadMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        id: Long
    ): Result<Unit> {
        return chatRepository.readMessage(
            id = id
        )
    }
}
