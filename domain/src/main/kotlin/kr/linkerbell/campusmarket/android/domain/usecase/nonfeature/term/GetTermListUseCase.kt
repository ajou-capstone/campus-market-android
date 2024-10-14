package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.term

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.term.Term
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TermRepository
import javax.inject.Inject

class GetTermListUseCase @Inject constructor(
    private val termRepository: TermRepository
) {
    suspend operator fun invoke(): Result<List<Term>> {
        return termRepository.getTermList()
    }
}
