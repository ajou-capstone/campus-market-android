package kr.linkerbell.campusmarket.android.domain.repository.nonfeature

import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Campus
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Profile

interface UserRepository {

    suspend fun getProfile(): Result<Profile>

    suspend fun setProfile(
        nickname: String,
        profileImage: String
    ): Result<Unit>

    suspend fun getAvailableCampusList(): Result<List<Campus>>

    suspend fun setCampus(
        id: Long
    ): Result<Unit>
}
