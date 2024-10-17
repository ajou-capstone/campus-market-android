package kr.linkerbell.campusmarket.android.data.repository.feature.trade

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.data.common.DEFAULT_PAGING_SIZE
import kr.linkerbell.campusmarket.android.data.remote.network.api.feature.TradeApi
import kr.linkerbell.campusmarket.android.data.repository.feature.trade.paging.SearchTradePagingSource
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class RealTradeRepository @Inject constructor(
    private val tradeApi: TradeApi
) : TradeRepository {

    override suspend fun searchTradeList(
        name: String,
        category: String,
        minPrice: Int,
        maxPrice: Int,
        sorted: String
    ): Flow<PagingData<Trade>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGING_SIZE,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                SearchTradePagingSource(
                    tradeApi = tradeApi,
                    name = name,
                    category = category,
                    minPrice = minPrice,
                    maxPrice = maxPrice,
                    sorted = sorted
                )
            },
        ).flow
    }
}
