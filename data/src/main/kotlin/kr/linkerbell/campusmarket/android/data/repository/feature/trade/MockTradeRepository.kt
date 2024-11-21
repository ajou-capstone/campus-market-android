package kr.linkerbell.campusmarket.android.data.repository.feature.trade

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kr.linkerbell.campusmarket.android.data.remote.local.database.searchhistory.SearchHistoryDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.searchhistory.SearchHistoryEntity
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.CategoryList
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.DeletedLikedItemInfo
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.LikedItemInfo
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeContents
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository
import timber.log.Timber

class MockTradeRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : TradeRepository {

    override suspend fun searchTradeList(
        name: String,
        category: String,
        minPrice: Int,
        maxPrice: Int,
        sorted: String
    ): Flow<PagingData<SummarizedTrade>> {
        randomShortDelay()

        return flowOf(
            PagingData.from(
                listOf(
                    SummarizedTrade(
                        itemId = 1L,
                        userId = 1L,
                        nickname = "장성혁",
                        thumbnail = "https://picsum.photos/200",
                        title = "콜라 팝니다",
                        price = 1000,
                        chatCount = 5,
                        likeCount = 2,
                        itemStatus = "",
                        isLiked = true
                    )
                )
            )
        )
    }

    override suspend fun getSearchHistoryList(): Flow<List<String>> {
        randomShortDelay()

        return flowOf(
            listOf("history1", "history22", "history333")
        )
    }

    override suspend fun deleteSearchHistoryByText(text: String) {
        randomShortDelay()

        Timber.tag("MockSearchHistoryRepository").d("call deleteByText(${text})")
    }

    override suspend fun deleteAllSearchHistory() {
        randomShortDelay()

        Timber.tag("MockSearchHistoryRepository").d("call deleteAll()")
    }

    override suspend fun addSearchHistory(text: String) {
        randomShortDelay()

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
        randomShortDelay()
        val newPostContent = "title = $title" +
                "description = $description" +
                "price = $price" +
                "category = $category" +
                "thumbnail = $thumbnail" +
                "images = $images"
        Timber.tag("MockSearchHistoryRepository").d("call postNewTrade(${newPostContent})")
        return Result.success(0L)
    }

    override suspend fun patchTradeContents(
        tradeContents: TradeContents,
        itemId: Long
    ): Result<Unit> {
        val newPostContent = "title = ${tradeContents.title}" +
                "description = ${tradeContents.description}" +
                "price = ${tradeContents.price}" +
                "category = ${tradeContents.category}" +
                "thumbnail = ${tradeContents.thumbnail}" +
                "images = ${tradeContents.images}"
        Timber.tag("MockSearchHistoryRepository").d("call patchTradeContents(${newPostContent})")
        return Result.success(Unit)
    }

    override suspend fun changeTradeStatus(
        itemStatus: String,
        itemId: Long,
        buyerId: Long
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getCategoryList(): Result<CategoryList> {
        return Result.success(CategoryList.empty)
    }

    override suspend fun searchTradeInfo(itemId: Long): Result<TradeInfo> {
        return Result.success(TradeInfo.empty)
    }

    override suspend fun postLikedItem(itemId: Long): Result<LikedItemInfo> {
        return Result.success(LikedItemInfo.empty)
    }

    override suspend fun deleteLikedItem(itemId: Long): Result<DeletedLikedItemInfo> {
        return Result.success(DeletedLikedItemInfo.empty)
    }

    override suspend fun deleteTradeInfo(itemId: Long): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun postUserRating(
        targetUserId: Long,
        itemId: Long,
        description: String,
        rating: Int
    ): Result<Unit> {
        return Result.success(Unit)
    }

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
