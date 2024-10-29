package kr.linkerbell.campusmarket.android.domain.repository.nonfeature

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.tradesearch.SearchHistory

interface SearchHistoryRepository {
    suspend fun getSearchHistory(): Result<SearchHistory>

    suspend fun deleteByText(text: String)

    suspend fun deleteAll()

    suspend fun insert(text: String)
}
