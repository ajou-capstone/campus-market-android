package kr.linkerbell.campusmarket.android.domain.repository.nonfeature

interface AuthenticationRepository {

    suspend fun sendEmailVerifyCode(
        email: String
    ): Result<String>

    suspend fun verifyEmailVerifyCode(
        token: String,
        verifyCode: String
    ): Result<Unit>

    suspend fun logout(): Result<Unit>

    suspend fun withdraw(): Result<Unit>
}
