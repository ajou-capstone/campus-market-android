package kr.linkerbell.campusmarket.android.domain.repository.nonfeature

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Campus
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile

interface UserRepository {

    suspend fun getMyProfile(): Result<MyProfile>

    suspend fun setMyProfile(
        nickname: String,
        profileImage: String
    ): Result<Unit>

    suspend fun getAvailableCampusList(): Result<List<Campus>>

    suspend fun setCampus(
        id: Long
    ): Result<Unit>

    suspend fun getUserProfile(
        id: Long
    ): Result<UserProfile>
}
