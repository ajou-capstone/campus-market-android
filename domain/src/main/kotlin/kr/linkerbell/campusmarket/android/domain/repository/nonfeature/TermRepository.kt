package kr.linkerbell.campusmarket.android.domain.repository.nonfeature

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.term.Term

interface TermRepository {
    suspend fun getTermList(): Result<List<Term>>

    suspend fun agreeTermList(
        termIdList: List<Long>
    ): Result<Unit>
}
