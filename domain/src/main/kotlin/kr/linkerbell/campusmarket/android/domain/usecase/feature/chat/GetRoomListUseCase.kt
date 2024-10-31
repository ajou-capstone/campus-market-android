package kr.linkerbell.campusmarket.android.domain.usecase.feature.chat

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.repository.feature.ChatRepository

class GetRoomListUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<Room>> {
        return chatRepository.getRoomList()
    }
}
