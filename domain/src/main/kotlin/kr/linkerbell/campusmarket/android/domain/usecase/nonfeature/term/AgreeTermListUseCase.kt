package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.term

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TermRepository

class AgreeTermListUseCase @Inject constructor(
    private val termRepository: TermRepository
) {
    suspend operator fun invoke(
        termIdList: List<Long>
    ): Result<Unit> {
        return termRepository.agreeTermList(
            termIdList = termIdList
        )
    }
}
