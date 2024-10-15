package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository
import javax.inject.Inject

class SetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        nickname: String,
        profileImage: String
    ): Result<Unit> {
        return userRepository.setProfile(
            nickname = nickname,
            profileImage = profileImage
        )
    }
}
