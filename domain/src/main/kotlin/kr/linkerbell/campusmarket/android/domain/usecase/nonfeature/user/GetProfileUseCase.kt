package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Profile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Profile> {
        return userRepository.getProfile()
    }
}
