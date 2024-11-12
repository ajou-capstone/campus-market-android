package kr.linkerbell.campusmarket.android.domain.model.feature.chat

sealed interface Message {
    data class Text(
        val id: Long,
        val chatRoomId: Long,
        val userId: Long,
        val content: String,
        val createdAt: Long
    ) : Message

    data class Image(
        val id: Long,
        val chatRoomId: Long,
        val userId: Long,
        val content: String,
        val createdAt: Long
    ) : Message

    data class Schedule(
        val id: Long,
        val chatRoomId: Long,
        val userId: Long,
        val createdAt: Long
    ) : Message
}
