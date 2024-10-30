package kr.linkerbell.campusmarket.android.data.repository.nonfeature.tradesearch

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.local.database.sample.SearchHistoryDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.sample.SearchHistoryEntity
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.tradesearch.SearchHistory
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SearchHistoryRepository
import timber.log.Timber

class MockSearchHistoryRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryRepository {
    override suspend fun getSearchHistory(): Result<SearchHistory> {
        return Result.success(
            SearchHistory(
                searchHistory = listOf("history1", "history22", "history333")
            )
        )
    }

    override suspend fun deleteByText(text: String) {
        Timber.tag("MockSearchHistoryRepository").d("call deleteByText(${text})")
    }

    override suspend fun deleteAll() {
        Timber.tag("MockSearchHistoryRepository").d("call deleteAll()")
    }

    override suspend fun insert(text: String) {
        searchHistoryDao.insert(SearchHistoryEntity(queryString = text))
    }

}
