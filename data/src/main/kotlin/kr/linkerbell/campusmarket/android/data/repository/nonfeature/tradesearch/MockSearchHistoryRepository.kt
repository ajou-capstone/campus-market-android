package kr.linkerbell.campusmarket.android.data.repository.nonfeature.tradesearch

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
}
