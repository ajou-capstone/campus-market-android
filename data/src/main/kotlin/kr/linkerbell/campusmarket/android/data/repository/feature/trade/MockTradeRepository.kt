package kr.linkerbell.campusmarket.android.data.repository.feature.trade

import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.Trade
import kr.linkerbell.campusmarket.android.domain.repository.feature.TradeRepository
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SearchHistoryRepository

class MockTradeRepository @Inject constructor() : TradeRepository {

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

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
