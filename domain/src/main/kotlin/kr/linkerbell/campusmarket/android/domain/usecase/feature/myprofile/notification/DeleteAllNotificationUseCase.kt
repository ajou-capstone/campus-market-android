package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.notification

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class DeleteAllNotificationUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return myPageRepository.deleteAllNotification()
    }
}
