package kr.linkerbell.campusmarket.android.data.repository.nonfeature.user

import kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature.UserApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Profile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository
import javax.inject.Inject

class RealUserRepository @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getProfile(): Result<Profile> {
        return userApi.getProfile().toDomain()
    }

    override suspend fun setProfile(
        nickname: String,
        profileImage: String
    ): Result<Unit> {
        return userApi.setProfile(
            nickname = nickname,
            profileImage = profileImage
        ).map { }
    }
}
