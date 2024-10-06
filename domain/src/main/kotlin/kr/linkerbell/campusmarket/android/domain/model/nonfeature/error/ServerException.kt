package kr.linkerbell.campusmarket.android.domain.model.nonfeature.error

open class ServerException(
    open val id: String,
    override val message: String
) : Exception(message)
