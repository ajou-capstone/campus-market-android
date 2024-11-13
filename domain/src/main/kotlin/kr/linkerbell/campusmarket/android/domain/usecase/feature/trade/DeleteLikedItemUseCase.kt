package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.DeletedLikedItemInfo
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class DeleteLikedItemUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(
        itemId: Long
    ): Result<DeletedLikedItemInfo> {
        return tradeRepository.deleteLikedItem(itemId)
    }
}
