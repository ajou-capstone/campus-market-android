package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.LikedItemInfo
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class PostLikedItemUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(
        itemId: Long
    ): Result<LikedItemInfo> {
        return tradeRepository.postLikedItem(itemId)
    }
}
