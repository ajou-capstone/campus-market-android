package kr.linkerbell.boardlink.android.data.remote.network.model.nonfeature.randomuserprofile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.boardlink.android.data.remote.mapper.DataMapper
import kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile.RandomUserProfile

@Serializable
data class RandomUserProfileRes(
    @SerialName("results")
    val results: List<RandomUserProfileInfo>
) : DataMapper<RandomUserProfile> {
    override fun toDomain(): RandomUserProfile {
        val randomUserProfileList: MutableList<RandomUserProfile> = mutableListOf()
        results.forEach { item ->
            randomUserProfileList.add(
                RandomUserProfile(
                    "${item.name.title} ${item.name.first} ${item.name.last}",
                    item.gender,
                    item.email
                )
            )
        }

        return randomUserProfileList[0]
    }
}

@Serializable
data class RandomUserProfileInfo(
    @SerialName("gender")
    val gender: String,
    @SerialName("name")
    val name: Name,
    @SerialName("email")
    val email: String
)

@Serializable
data class Name(
    val title: String,
    val first: String,
    val last: String
)
