package kr.linkerbell.campusmarket.android.data.repository.feature.trade.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kr.linkerbell.campusmarket.android.data.common.DEFAULT_PAGE_START
import kr.linkerbell.campusmarket.android.data.remote.network.api.feature.TradeApi
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.SummarizedTrade

class SearchTradePagingSource(
    private val tradeApi: TradeApi,
    private val name: String,
    private val category: String,
    private val minPrice: Int,
    private val maxPrice: Int,
    private val sorted: String,
) : PagingSource<Int, SummarizedTrade>() {

    override fun getRefreshKey(state: PagingState<Int, SummarizedTrade>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SummarizedTrade> {
        val pageNum = params.key ?: DEFAULT_PAGE_START
        val pageSize = params.loadSize

        return tradeApi.searchTradeList(
            name = name,
            category = category,
            minPrice = minPrice,
            maxPrice = maxPrice,
            sorted = sorted,
            pageNum = pageNum,
            pageSize = pageSize
        ).map { data ->
            val nextPage = if (data.hasNext) pageNum + 1 else null
            val previousPage = if (data.hasPrevious) pageNum - 1 else null

            LoadResult.Page(
                data = data.content.map { it.toDomain() },
                prevKey = previousPage,
                nextKey = nextPage
            )
        }.getOrElse { exception ->
            LoadResult.Error(exception)
        }
    }
}
