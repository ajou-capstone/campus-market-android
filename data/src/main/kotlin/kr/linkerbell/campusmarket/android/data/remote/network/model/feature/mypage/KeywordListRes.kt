package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.mypage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.Keyword

@Serializable
data class KeywordListRes(
    @SerialName("keywordList")
    val keywordList: List<KeywordListItemRes>
) : DataMapper<List<Keyword>> {
    override fun toDomain(): List<Keyword> {
        return keywordList.map { keywordItem ->
            Keyword(
                id = keywordItem.keywordId,
                keyword = keywordItem.keywordName
            )
        }
    }
}

@Serializable
data class KeywordListItemRes(
    @SerialName("keywordId")
    val keywordId: Long,
    @SerialName("keywordName")
    val keywordName: String
)

