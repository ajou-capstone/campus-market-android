package kr.linkerbell.campusmarket.android.domain.usecase.feature.chat

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.ChatRepository

class SetRoomNotificationUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        id: Long,
        isAlarm: Boolean
    ): Result<Unit> {
        return chatRepository.setRoomNotification(
            id = id,
            isAlarm = isAlarm
        )
    }
}
