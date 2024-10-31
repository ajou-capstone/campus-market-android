package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tradesearch

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.tradesearch.SearchHistory
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SearchHistoryRepository

class GetSearchHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(): Result<SearchHistory> {
        return searchHistoryRepository.getSearchHistory()
    }
}

