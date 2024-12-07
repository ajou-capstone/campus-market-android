package kr.linkerbell.campusmarket.android.data.repository.feature.trade

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.linkerbell.campusmarket.android.data.common.DEFAULT_PAGING_SIZE
import kr.linkerbell.campusmarket.android.data.remote.local.database.searchhistory.SearchHistoryDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.searchhistory.SearchHistoryEntity
import kr.linkerbell.campusmarket.android.data.remote.network.api.feature.TradeApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.data.repository.feature.trade.paging.SearchTradePagingSource
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.CategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.DeletedLikedItemInfo
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.LikedItemInfo
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeContents
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository

class RealTradeRepository @Inject constructor(
    private val tradeApi: TradeApi,
    private val searchHistoryDao: SearchHistoryDao
) : TradeRepository {

    override suspend fun searchTradeList(
        name: String,
        category: String,
        minPrice: Int,
        maxPrice: Int,
        sorted: String,
        itemStatus: String
    ): Flow<PagingData<SummarizedTrade>> {
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
                    sorted = sorted,
                    itemStatus = itemStatus
                )
            },
        ).flow
    }

    override suspend fun getSearchHistoryList(): Flow<List<String>> {
        return searchHistoryDao.getAll().map { searchHistoryList ->
            searchHistoryList.map {
                it.toDomain()
            }
        }
    }

    override suspend fun deleteSearchHistoryByText(text: String) {
        searchHistoryDao.deleteByText(text)
    }

    override suspend fun deleteAllSearchHistory() {
        searchHistoryDao.deleteAll()
    }

    override suspend fun addSearchHistory(text: String) {
        searchHistoryDao.insert(SearchHistoryEntity(queryString = text))
    }

    override suspend fun postTradeContents(
        title: String,
        description: String,
        price: Int,
        category: String,
        thumbnail: String,
        images: List<String>
    ): Result<Long> {
        return tradeApi.postNewTrade(title, description, price, category, thumbnail, images)
            .toDomain()
    }

    override suspend fun patchTradeContents(
        tradeContents: TradeContents,
        itemId: Long
    ): Result<Unit> {
        return tradeApi.patchTradeContents(tradeContents, itemId)
    }

    override suspend fun changeTradeStatus(
        itemStatus: String,
        itemId: Long,
        buyerId: Long
    ): Result<Unit> {
        return tradeApi.changeTradeStatus(
            itemStatus = itemStatus,
            itemId = itemId,
            buyerId = buyerId
        )
    }

    override suspend fun getCategoryList(): Result<CategoryList> {
        return tradeApi.getCategoryList().toDomain()
    }

    override suspend fun searchTradeInfo(itemId: Long): Result<TradeInfo> {
        return tradeApi.getTradeInfo(itemId).toDomain()
    }

    override suspend fun postLikedItem(itemId: Long): Result<LikedItemInfo> {
        return tradeApi.postLikeItem(itemId).toDomain()
    }

    override suspend fun deleteLikedItem(itemId: Long): Result<DeletedLikedItemInfo> {
        return tradeApi.deleteLikedItem(itemId).toDomain()
    }

    override suspend fun deleteTradeInfo(itemId: Long): Result<Unit> {
        return tradeApi.deleteTradeInfo(itemId)
    }

    override suspend fun postUserRating(
        targetUserId: Long,
        itemId: Long,
        description: String,
        rating: Int
    ): Result<Unit> {
        return tradeApi.postUserRating(
            targetUserId,
            itemId,
            description,
            rating
        )
    }
}
