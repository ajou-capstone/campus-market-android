package kr.linkerbell.campusmarket.android.data.repository.nonfeature.tradesearch

import android.util.Log
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.tradesearch.SearchHistory
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SearchHistoryRepository

class MockSearchHistoryRepository @Inject constructor(
) : SearchHistoryRepository {
    override suspend fun getSearchHistory(): Result<SearchHistory> {
        return Result.success(
            SearchHistory(
                searchHistory = listOf("history1", "history22", "history333")
            )
        )
    }

    override suspend fun deleteByText(text: String) {
        Log.i("MockSearchHistoryRepository", "call deleteByText(${text})")
    }

    override suspend fun deleteAll() {
        Log.i("MockSearchHistoryRepository", "call deleteAll()")
    }


}
