package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tradesearch

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.SearchHistoryRepository

class DeleteAllSearchHistoryByTextUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke() {
        searchHistoryRepository.deleteAll()
    }
}

