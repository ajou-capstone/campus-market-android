package kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.file.PreSignedUrl

@Serializable
data class GetPreSignedUrlRes(
    @SerialName("presignedUrl")
    val presignedUrl: String,
    @SerialName("s3url")
    val s3url: String
) : DataMapper<PreSignedUrl> {
    override fun toDomain(): PreSignedUrl {
        return PreSignedUrl(
            preSignedUrl = presignedUrl,
            s3url = s3url
        )
    }
}
