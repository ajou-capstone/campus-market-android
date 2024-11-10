package kr.linkerbell.campusmarket.android.domain.usecase.feature.trade

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class SearchTradeListUseCase @Inject constructor(
    private val tradeRepository: TradeRepository
) {
    suspend operator fun invoke(
        name: String,
        category: String,
        minPrice: Int,
        maxPrice: Int,
        sorted: String
    ): Flow<PagingData<SummarizedTrade>> {
        return tradeRepository.searchTradeList(
            name = name,
            category = category,
            minPrice = minPrice,
            maxPrice = maxPrice,
            sorted = sorted
        )
    }
}
