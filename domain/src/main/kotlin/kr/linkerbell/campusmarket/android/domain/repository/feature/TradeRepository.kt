package kr.linkerbell.campusmarket.android.domain.repository.feature

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.CategoryList
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

    suspend fun postNewTrade(
        title: String,
        description: String,
        price: Int,
        category: String,
        thumbnail: String,
        images: List<String>
    ): Result<Long>

    suspend fun getCategoryList(): Result<CategoryList>
}
