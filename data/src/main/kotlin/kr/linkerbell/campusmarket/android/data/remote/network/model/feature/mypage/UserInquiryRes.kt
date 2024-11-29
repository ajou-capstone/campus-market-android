package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserInquiry

@Serializable
data class UserInquiryListRes(
    @SerialName("content")
    val content: List<UserInquiryListItemRes>,
    @SerialName("sort")
    val sort: UserInquirySortRes,
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("hasPrevious")
    val hasPrevious: Boolean,
    @SerialName("hasNext")
    val hasNext: Boolean,
)

@Serializable
data class UserInquirySortRes(
    @SerialName("empty")
    val empty: Boolean,
    @SerialName("sorted")
    val sorted: Boolean,
    @SerialName("unsorted")
    val unsorted: Boolean,
)

@Serializable
data class UserInquiryListItemRes(
    @SerialName("qaId")
    val qaId: Long,
    @SerialName("userId")
    val userId: Long,
    @SerialName("category")
    val category: String,
    @SerialName("title")
    val title: String,
    @SerialName("isCompleted")
    val isCompleted: Boolean,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("answerDate")
    val answerDate: String?

) : DataMapper<UserInquiry> {
    override fun toDomain(): UserInquiry {
        val defaultDate = "1970-01-01T00:00:00".toLocalDateTime()
        return UserInquiry(
            qaId = qaId,
            userId = userId,
            category = category,
            title = title,
            isCompleted = isCompleted,
            createdDate = LocalDateTime.parse(createdDate),
            answerDate = answerDate?.let { LocalDateTime.parse(it) } ?: defaultDate
        )
    }
}
