package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository

class SetFcmTokenUseCase @Inject constructor(
    private val trackingRepository: TrackingRepository
) {
    suspend operator fun invoke(
        token: String
    ) {
        return trackingRepository.setFcmToken(
            token = token
        )
    }
}
