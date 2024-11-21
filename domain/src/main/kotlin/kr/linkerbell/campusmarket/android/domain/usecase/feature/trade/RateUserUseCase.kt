package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class RateUserUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(
        targetUserId: Long,
        itemId: Long,
        description: String,
        rating: Int
    ): Result<Unit> {
        return tradeRepository.postUserRating(
            targetUserId = targetUserId,
            itemId = itemId,
            description = description,
            rating = rating
        )
    }
}
