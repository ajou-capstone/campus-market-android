package kr.linkerbell.campusmarket.android.data.repository.nonfeature.file

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature.FileApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.file.PreSignedUrl
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.FileRepository

class RealFileRepository @Inject constructor(
    private val fileApi: FileApi
) : FileRepository {
    override suspend fun getPreSignedUrl(
        fileName: String
    ): Result<PreSignedUrl> {
        return fileApi.getPreSignedUrl(
            fileName = fileName
        ).toDomain()
    }

    override suspend fun upload(
        preSignedUrl: String,
        imageUri: String
    ): Result<Unit> {
        return fileApi.upload(
            preSignedUrl = preSignedUrl,
            imageUri = imageUri
        )
    }
}
