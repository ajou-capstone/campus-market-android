package kr.linkerbell.campusmarket.android.domain.repository.feature

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade

interface TradeRepository {

    suspend fun searchTradeList(
        name: String,
        category: String,
        minPrice: Int,
        maxPrice: Int,
        sorted: String
    ): Flow<PagingData<Trade>>

    suspend fun getSearchHistoryList(): Flow<List<String>>

    suspend fun deleteSearchHistoryByText(text: String)

    suspend fun deleteAllSearchHistory()

    suspend fun addSearchHistory(text: String)
}
