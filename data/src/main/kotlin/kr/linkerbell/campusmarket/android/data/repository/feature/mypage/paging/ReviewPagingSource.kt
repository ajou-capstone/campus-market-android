package kr.linkerbell.campusmarket.android.data.repository.feature.mypage.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kr.linkerbell.campusmarket.android.data.common.DEFAULT_PAGE_START
import kr.linkerbell.campusmarket.android.data.remote.network.api.feature.MyPageApi
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview

class ReviewPagingSource(
    private val myPageApi: MyPageApi,
    private val userId: Long
) : PagingSource<Int, UserReview>() {

    override fun getRefreshKey(state: PagingState<Int, UserReview>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserReview> {
        val pageNum = params.key ?: DEFAULT_PAGE_START
        val pageSize = params.loadSize

        return myPageApi.getUserReviews(
            userId = userId,
            page = pageNum,
            size = pageSize
        ).map { data ->
            val nextPage = if (data.hasNext) pageNum + 1 else null
            val previousPage = if (data.hasPrevious) pageNum - 1 else null

            LoadResult.Page(
                data = data.reviewList.map { it.toDomain() },
                prevKey = previousPage,
                nextKey = nextPage
            )
        }.getOrElse { exception ->
            LoadResult.Error(exception)
        }
    }
}
