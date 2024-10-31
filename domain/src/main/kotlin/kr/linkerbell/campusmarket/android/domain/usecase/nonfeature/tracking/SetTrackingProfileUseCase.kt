package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tracking

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository

class SetTrackingProfileUseCase @Inject constructor(
    private val trackingRepository: TrackingRepository
) {
    suspend operator fun invoke(
        myProfile: MyProfile
    ): Result<Unit> {
        return trackingRepository.setProfile(
            myProfile = myProfile
        )
    }
}
