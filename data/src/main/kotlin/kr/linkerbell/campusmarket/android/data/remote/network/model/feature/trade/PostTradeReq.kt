package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.trade

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostTradeReq(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("price")
    val price: Int,
    @SerialName("category")
    val category: String,
    @SerialName("thumbnail")
    val thumbnail: String,
    @SerialName("images")
    val images: List<String>
)
