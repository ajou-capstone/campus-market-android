package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository
import javax.inject.Inject

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
