package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Campus

@Serializable
data class GetAvailableCampusListRes(
    @SerialName("campuses")
    val campuses: List<GetAvailableCampusListItemRes>
) : DataMapper<List<Campus>> {
    override fun toDomain(): List<Campus> {
        return campuses.map { it.toDomain() }
    }
}

@Serializable
data class GetAvailableCampusListItemRes(
    @SerialName("id")
    val id: Long,
    @SerialName("region")
    val region: String
) : DataMapper<Campus> {
    override fun toDomain(): Campus {
        return Campus(
            id = id,
            region = region
        )
    }
}
