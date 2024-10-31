package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository

class SetMyProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        nickname: String,
        profileImage: String
    ): Result<Unit> {
        return userRepository.setMyProfile(
            nickname = nickname,
            profileImage = profileImage
        )
    }
}
