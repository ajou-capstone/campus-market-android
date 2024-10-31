package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository

class SetCampusUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        id: Long
    ): Result<Unit> {
        return userRepository.setCampus(
            id = id
        )
    }
}
