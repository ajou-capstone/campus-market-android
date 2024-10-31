package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.file.PreSignedUrl
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.FileRepository

class GetPreSignedUrlUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {
    suspend operator fun invoke(
        fileName: String
    ): Result<PreSignedUrl> {
        return fileRepository.getPreSignedUrl(
            fileName = fileName
        )
    }
}
