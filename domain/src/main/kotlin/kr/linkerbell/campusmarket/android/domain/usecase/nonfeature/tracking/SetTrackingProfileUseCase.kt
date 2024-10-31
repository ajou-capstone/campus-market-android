package kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.tracking

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.TrackingRepository
import javax.inject.Inject

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
