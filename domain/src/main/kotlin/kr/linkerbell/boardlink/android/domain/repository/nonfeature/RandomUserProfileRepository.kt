package kr.linkerbell.boardlink.android.domain.repository.nonfeature

import kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile.RandomUserProfile

interface RandomUserProfileRepository {

    suspend fun getRandomUserProfile(): Result<RandomUserProfile>
}
