package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.history

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class GetSearchHistoryListUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(): Flow<List<String>> {
        return tradeRepository.getSearchHistoryList()
    }
}
