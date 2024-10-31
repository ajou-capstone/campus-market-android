package kr.linkerbell.campusmarket.android.data.repository.nonfeature.term

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature.TermApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.term.Term
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TermRepository

class RealTermRepository @Inject constructor(
    private val termApi: TermApi
) : TermRepository {

    override suspend fun getTermList(): Result<List<Term>> {
        return termApi.getTermList().toDomain()
    }

    override suspend fun agreeTermList(
        termIdList: List<Long>
    ): Result<Unit> {
        return termApi.agreeTermList(termIdList)
    }
}
