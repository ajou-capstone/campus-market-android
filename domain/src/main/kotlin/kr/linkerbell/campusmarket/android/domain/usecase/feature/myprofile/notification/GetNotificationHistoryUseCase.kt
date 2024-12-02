package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile.notification

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserNotification
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetNotificationHistoryUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Flow<PagingData<UserNotification>> {
        return myPageRepository.getNotificationHistory()
    }
}
