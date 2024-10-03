package kr.linkerbell.boardlink.android.domain.usecase.nonfeature.randomuserprofile

import javax.inject.Inject
import kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile.RandomUserProfile
import kr.linkerbell.boardlink.android.domain.repository.nonfeature.RandomUserProfileRepository

class GetRandomUserProfileUseCase @Inject constructor(
    private val randomUserProfileRepository: RandomUserProfileRepository
) {
    suspend operator fun invoke(): Result<RandomUserProfile> {
        return randomUserProfileRepository.getRandomUserProfile()
    }

}
