package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class DeleteTradeInfoUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
){
    suspend operator fun invoke(
        itemId: Long
    ): Result<Unit> {
        return tradeRepository.deleteTradeInfo(
            itemId = itemId
        )
    }
}
