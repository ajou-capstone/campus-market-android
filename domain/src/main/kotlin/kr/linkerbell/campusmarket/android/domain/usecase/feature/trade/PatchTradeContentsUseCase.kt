package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeContents
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class PatchTradeContentsUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(
        tradeContents: TradeContents,
        itemId: Long
    ): Result<Unit> {
        return tradeRepository.patchTradeContents(tradeContents, itemId)
    }
}
