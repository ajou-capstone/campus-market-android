package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class GetTradeInfoUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(
        itemId: Long
    ): Result<TradeInfo> {
        return tradeRepository.searchTradeInfo(
            itemId = itemId
        )
    }
}
