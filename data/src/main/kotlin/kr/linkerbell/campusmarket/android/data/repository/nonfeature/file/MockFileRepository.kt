package kr.linkerbell.campusmarket.android.data.repository.nonfeature.file

import kotlinx.coroutines.delay
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.file.PreSignedUrl
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.FileRepository
import javax.inject.Inject

class MockFileRepository @Inject constructor() : FileRepository {
    override suspend fun getPreSignedUrl(
        fileName: String
    ): Result<PreSignedUrl> {
        randomShortDelay()
        return Result.success(
            PreSignedUrl(
                presignedUrl = "https://www.naver.com",
                s3url = "https://www.naver.com"
            )
        )
    }

    override suspend fun upload(
        preSignedUrl: String,
        imageUri: String
    ): Result<Unit> {
        randomLongDelay()
        return Result.success(Unit)
    }

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
