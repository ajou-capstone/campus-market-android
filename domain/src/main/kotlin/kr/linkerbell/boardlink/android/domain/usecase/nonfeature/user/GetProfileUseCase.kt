package kr.linkerbell.boardlink.android.domain.usecase.nonfeature.user

import kr.linkerbell.boardlink.android.domain.model.nonfeature.user.Profile
import kr.linkerbell.boardlink.android.domain.repository.nonfeature.UserRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Profile> {
        return userRepository.getProfile()
    }
}
