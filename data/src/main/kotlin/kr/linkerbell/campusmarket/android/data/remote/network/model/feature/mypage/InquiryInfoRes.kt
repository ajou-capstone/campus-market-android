package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.InquiryInfo

@Serializable
data class InquiryInfoRes(
    @SerialName("qaId")
    val qaId: Long,
    @SerialName("userId")
    val userId: Long,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("answerDescription")
    val answerDescription: String?,
    @SerialName("category")
    val category: String,
    @SerialName("isCompleted")
    val isCompleted: Boolean,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("answerDate")
    val answerDate: String?
) : DataMapper<InquiryInfo> {
    override fun toDomain(): InquiryInfo {
        val defaultDate = "1970-01-01T00:00:00".toLocalDateTime()
        return InquiryInfo(
            qaId = qaId,
            userId = userId,
            title = title,
            description = description,
            answerDescription = answerDescription ?: "",
            category = category,
            isCompleted = isCompleted,
            createdDate = LocalDateTime.parse(createdDate),
            answerDate = answerDate?.let { LocalDateTime.parse(it) } ?: defaultDate
        )
    }
}
