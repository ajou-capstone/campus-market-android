package kr.linkerbell.campusmarket.android.domain.usecase.feature.chat

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.ChatRepository

class QuitRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        id: Long
    ): Result<Unit> {
        return chatRepository.quitRoom(
            id = id
        )
    }
}
