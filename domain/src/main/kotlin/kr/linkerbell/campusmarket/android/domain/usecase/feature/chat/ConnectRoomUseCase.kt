package kr.linkerbell.campusmarket.android.domain.usecase.feature.chat

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Session
import kr.linkerbell.campusmarket.android.domain.repository.feature.ChatRepository

class ConnectRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Result<Session> {
        return chatRepository.connectRoom()
    }
}
