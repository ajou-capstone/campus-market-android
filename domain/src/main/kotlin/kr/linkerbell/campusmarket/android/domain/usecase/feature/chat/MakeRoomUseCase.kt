package kr.linkerbell.campusmarket.android.domain.usecase.feature.chat

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.repository.feature.ChatRepository

class MakeRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        userId: Long,
        tradeId: Long
    ): Result<Room> {
        return chatRepository.makeRoom(
            userId = userId,
            tradeId = tradeId
        )
    }
}
