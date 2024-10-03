package kr.linkerbell.boardlink.android.data.repository.nonfeature.randomuserprofile

import kr.linkerbell.boardlink.android.data.remote.network.api.nonfeature.RandomUserApi
import javax.inject.Inject
import kr.linkerbell.boardlink.android.data.remote.network.util.toDomain
import kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile.RandomUserProfile
import kr.linkerbell.boardlink.android.domain.repository.nonfeature.RandomUserProfileRepository

class RealRandomUserProfileRepository @Inject constructor(
    private val randomUserApi: RandomUserApi
) : RandomUserProfileRepository {
    override suspend fun getRandomUserProfile(): Result<RandomUserProfile> {
        return randomUserApi.getRandomUserProfile().toDomain()
    }
}
