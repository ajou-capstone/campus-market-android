package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file

import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.FileRepository
import javax.inject.Inject

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
