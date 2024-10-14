package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.term

import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TermRepository
import javax.inject.Inject

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
