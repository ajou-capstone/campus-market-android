package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tracking

import androidx.annotation.Size
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository

class LogEventUseCase @Inject constructor(
    private val trackingRepository: TrackingRepository
) {
    suspend operator fun invoke(
        @Size(min = 1, max = 40) eventName: String,
        params: Map<String, Any>
    ): Result<Unit> {
        return trackingRepository.logEvent(
            eventName = eventName,
            params = params
        )
    }
}
