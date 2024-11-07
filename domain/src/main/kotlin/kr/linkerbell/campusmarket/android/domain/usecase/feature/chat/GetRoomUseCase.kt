package kr.linkerbell.campusmarket.android.domain.usecase.feature.chat

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.repository.feature.ChatRepository

class GetRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        id: Long
    ): Flow<Room> {
        return chatRepository.getRoom(
            id = id
        )
    }
}
