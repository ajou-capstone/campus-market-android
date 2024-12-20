package kr.linkerbell.campusmarket.android.domain.usecase.feature.myprofile

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.repository.feature.MyPageRepository

class GetRecentTradeListUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(
        userId: Long,
        type: String
    ): Flow<PagingData<RecentTrade>> {
        return myPageRepository.getRecentTrades(userId = userId, type = type)
    }
}
