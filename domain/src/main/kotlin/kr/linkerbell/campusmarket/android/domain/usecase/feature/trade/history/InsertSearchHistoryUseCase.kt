package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.history

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class InsertSearchHistoryUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(text: String) {
        tradeRepository.addSearchHistory(text)
    }
}
