package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.term

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.term.Term

@Serializable
data class GetTermListRes(
    @SerialName("terms")
    val terms: List<GetTermListItemRes>
) : DataMapper<List<Term>> {
    override fun toDomain(): List<Term> {
        return terms.map { it.toDomain() }
    }
}

@Serializable
data class GetTermListItemRes(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("url")
    val url: String,
    @SerialName("isRequired")
    val isRequired: Boolean,
    @SerialName("isAgree")
    val isAgree: Boolean,
) : DataMapper<Term> {
    override fun toDomain(): Term {
        return Term(
            id = id,
            title = title,
            url = url,
            isRequired = isRequired,
            isAgree = isAgree
        )
    }
}
