package kr.linkerbell.campusmarket.android.data.repository.nonfeature.term

import javax.inject.Inject
import kotlinx.coroutines.delay
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.term.Term
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TermRepository

class MockTermRepository @Inject constructor() : TermRepository {

    override suspend fun getTermList(): Result<List<Term>> {
        randomShortDelay()
        return Result.success(
            listOf(
                Term(
                    id = 0L,
                    title = "개인정보 수집 이용동의",
                    url = "https://www.naver.com",
                    isRequired = true,
                    isAgree = true
                ),
                Term(
                    id = 1L,
                    title = "고유식별 정보처리 동의",
                    url = "https://www.naver.com",
                    isRequired = true,
                    isAgree = true
                ),
                Term(
                    id = 2L,
                    title = "통신사 이용약관 동의",
                    url = "https://www.naver.com",
                    isRequired = true,
                    isAgree = true
                ),
                Term(
                    id = 3L,
                    title = "서비스 이용약관 동의",
                    url = "https://www.naver.com",
                    isRequired = false,
                    isAgree = true
                )
            )
        )
    }

    override suspend fun agreeTermList(
        termIdList: List<Long>
    ): Result<Unit> {
        randomShortDelay()
        return Result.success(Unit)
    }

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
