package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository
import javax.inject.Inject

class GetMyProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<MyProfile> {
        return userRepository.getMyProfile()
    }
}
