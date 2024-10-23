package kr.linkerbell.campusmarket.android.domain.model.feature.chat

data class Room(
    val id: Long,
    val userId: Long,
    val tradeId: Long,
    val readLatestMessageId: Long,
    val title: String,
    val isAlarm: Boolean
)
