package kr.linkerbell.boardlink.android.data.repository.nonfeature.user

import javax.inject.Inject
import kr.linkerbell.boardlink.android.data.remote.network.api.nonfeature.UserApi
import kr.linkerbell.boardlink.android.data.remote.network.util.toDomain
import kr.linkerbell.boardlink.android.domain.model.nonfeature.user.Profile
import kr.linkerbell.boardlink.android.domain.repository.nonfeature.UserRepository

class RealUserRepository @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getProfile(): Result<Profile> {
        return userApi.getProfile().toDomain()
    }
}
