package kr.linkerbell.campusmarket.android.data.repository.nonfeature.tradesearch

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.tradesearch.SearchHistory
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SearchHistoryRepository

class RealSearchHistoryRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SearchHistoryRepository {
    override suspend fun getSearchHistory(): Result<SearchHistory> {
        TODO("Room에서 사용자 검색 히스토리 가져오는 로직")
    }
}
