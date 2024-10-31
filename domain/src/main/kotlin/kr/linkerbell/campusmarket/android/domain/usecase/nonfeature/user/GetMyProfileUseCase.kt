package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository

class GetMyProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<MyProfile> {
        return userRepository.getMyProfile()
    }
}
