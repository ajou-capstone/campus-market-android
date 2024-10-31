package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.FileRepository

class UploadImageUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(
        preSignedUrl: String,
        imageUri: String
    ): Result<Unit> {
        return fileRepository.upload(
            preSignedUrl = preSignedUrl,
            imageUri = imageUri
        )
    }
}
