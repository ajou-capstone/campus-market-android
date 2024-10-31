package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        id: Long
    ): Result<UserProfile> {
        return userRepository.getUserProfile(
            id = id
        )
    }
}
