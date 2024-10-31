package kr.linkerbell.campusmarket.android.data.repository.feature.trade

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kr.linkerbell.campusmarket.android.data.remote.local.database.searchhistory.SearchHistoryDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.searchhistory.SearchHistoryEntity
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade
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
    ): Flow<PagingData<Trade>> {
        randomShortDelay()

        return flowOf(
            PagingData.from(
                listOf(
                    Trade(
                        itemId = 1L,
                        userId = 1L,
                        nickname = "장성혁",
                        thumbnail = "https://picsum.photos/200",
                        title = "콜라 팝니다",
                        price = 1000,
                        chatCount = 5,
                        likeCount = 2,
                        itemStatus = ""
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

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
