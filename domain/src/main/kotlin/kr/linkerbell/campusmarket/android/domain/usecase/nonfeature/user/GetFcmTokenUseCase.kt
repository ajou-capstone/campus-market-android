package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user

import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository
import javax.inject.Inject

class GetFcmTokenUseCase @Inject constructor(
    private val trackingRepository: TrackingRepository
) {
    suspend operator fun invoke(): String {
        return trackingRepository.getFcmToken()
    }
}
