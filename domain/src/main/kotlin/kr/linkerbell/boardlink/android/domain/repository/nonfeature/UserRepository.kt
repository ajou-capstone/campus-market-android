package kr.linkerbell.boardlink.android.domain.repository.nonfeature

import kr.linkerbell.boardlink.android.domain.model.nonfeature.user.Profile

interface UserRepository {

    suspend fun getProfile(): Result<Profile>
}
