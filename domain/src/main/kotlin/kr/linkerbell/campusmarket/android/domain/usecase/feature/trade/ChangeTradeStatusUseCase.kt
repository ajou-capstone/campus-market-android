package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class ChangeTradeStatusUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(
        itemStatus: String,
        itemId: Long,
        buyerId: Long
    ): Result<Unit> {
        return tradeRepository.changeTradeStatus(
            itemStatus = itemStatus,
            itemId = itemId,
            buyerId = buyerId
        )
    }
}
