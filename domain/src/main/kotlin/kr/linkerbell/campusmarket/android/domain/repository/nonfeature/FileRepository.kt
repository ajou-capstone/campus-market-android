package kr.linkerbell.campusmarket.android.domain.repository.nonfeature

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.file.PreSignedUrl

interface FileRepository {
    suspend fun getPreSignedUrl(
        fileName: String
    ): Result<PreSignedUrl>

    suspend fun upload(
        preSignedUrl: String,
        imageUri: String
    ): Result<Unit>
}
