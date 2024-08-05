package kr.linkerbell.boardlink.android.domain.usecase.nonfeature.tracking

import kr.linkerbell.boardlink.android.domain.model.nonfeature.user.Profile
import kr.linkerbell.boardlink.android.domain.repository.nonfeature.TrackingRepository
import javax.inject.Inject

class SetTrackingProfileUseCase @Inject constructor(
    private val trackingRepository: TrackingRepository
) {
    suspend operator fun invoke(
        profile: Profile
    ): Result<Unit> {
        return trackingRepository.setProfile(
            profile = profile
        )
    }
}
