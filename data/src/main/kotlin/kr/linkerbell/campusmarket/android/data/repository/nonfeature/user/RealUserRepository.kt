package kr.linkerbell.campusmarket.android.data.repository.nonfeature.user

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature.UserApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Campus
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.domain.repository.nonfeature.UserRepository

class RealUserRepository @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getMyProfile(): Result<MyProfile> {
        return userApi.getMyProfile().toDomain()
    }

    override suspend fun setMyProfile(
        nickname: String,
        profileImage: String
    ): Result<Unit> {
        return userApi.setMyProfile(
            nickname = nickname,
            profileImage = profileImage
        )
    }

    override suspend fun getAvailableCampusList(): Result<List<Campus>> {
        return userApi.getAvailableCampusList().toDomain()
    }

    override suspend fun setCampus(
        id: Long
    ): Result<Unit> {
        return userApi.setCampus(
            id = id
        )
    }

    override suspend fun getUserProfile(
        id: Long
    ): Result<UserProfile> {
        return userApi.getUserProfile(
            id = id
        ).toDomain()
    }
}
