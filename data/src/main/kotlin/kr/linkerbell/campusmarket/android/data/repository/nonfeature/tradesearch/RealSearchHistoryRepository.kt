package kr.linkerbell.campusmarket.android.data.repository.nonfeature.tradesearch

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.local.database.sample.SearchHistoryDao
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.tradesearch.SearchHistory
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SearchHistoryRepository

class RealSearchHistoryRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryRepository {

    override suspend fun getSearchHistory(): Result<SearchHistory> {
        return try {
            val searchHistoryEntities = searchHistoryDao.getAll()
            val queries = searchHistoryEntities.map { it.queryString }
            Result.success(SearchHistory(queries))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteByText(text: String) {
        searchHistoryDao.deleteByText(text)
    }

    override suspend fun deleteAll() {
        searchHistoryDao.deleteAll()
    }
}


