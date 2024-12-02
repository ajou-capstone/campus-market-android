package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.notification

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class DeleteNotificationByIdUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        notificationId: Long
    ): Result<Unit> {
        return myPageRepository.deleteNotificationById(
            notificationId = notificationId
        )
    }
}
