package kr.linkerbell.campusmarket.android.domain.repository.feature

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.CategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.DeletedLikedItemInfo
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.LikedItemInfo
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeContents
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo

interface TradeRepository {

    suspend fun searchTradeList(
        name: String,
        category: String,
        minPrice: Int,
        maxPrice: Int,
        sorted: String,
        itemStatus: String
    ): Flow<PagingData<SummarizedTrade>>

    suspend fun getSearchHistoryList(): Flow<List<String>>

    suspend fun deleteSearchHistoryByText(text: String)

    suspend fun deleteAllSearchHistory()

    suspend fun addSearchHistory(text: String)

    suspend fun postTradeContents(
        title: String,
        description: String,
        price: Int,
        category: String,
        thumbnail: String,
        images: List<String>
    ): Result<Long>

    suspend fun patchTradeContents(tradeContents: TradeContents, itemId: Long): Result<Unit>

    suspend fun changeTradeStatus(
        itemStatus: String,
        itemId: Long,
        buyerId: Long
    ): Result<Unit>

    suspend fun getCategoryList(): Result<CategoryList>

    suspend fun searchTradeInfo(itemId: Long): Result<TradeInfo>

    suspend fun postLikedItem(itemId: Long): Result<LikedItemInfo>

    suspend fun deleteLikedItem(itemId: Long): Result<DeletedLikedItemInfo>

    suspend fun deleteTradeInfo(itemId: Long): Result<Unit>

    suspend fun postUserRating(
        targetUserId: Long,
        itemId: Long,
        description: String,
        rating: Int
    ): Result<Unit>
}
