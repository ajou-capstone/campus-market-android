package kr.linkerbell.boardlink.android.domain.model.nonfeature.error

class InternalServerException(
    override val id: String,
    override val message: String
) : ServerException(id, message)
