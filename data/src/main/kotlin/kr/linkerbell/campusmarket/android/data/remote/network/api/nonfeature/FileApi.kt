package kr.linkerbell.campusmarket.android.data.remote.network.api.nonfeature

import android.net.Uri
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import java.io.File
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.di.NoAuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.file.GetPreSignedUrlRes
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert

class FileApi @Inject constructor(
    @NoAuthHttpClient private val noAuthClient: HttpClient,
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun getPreSignedUrl(
        fileName: String
    ): Result<GetPreSignedUrlRes> {
        return client.get("$baseUrl/api/v1/s3/presigned-url") {
            parameter("fileName", fileName)
        }.convert(errorMessageMapper::map)
    }

    suspend fun upload(
        preSignedUrl: String,
        imageUri: String
    ): Result<Unit> {
        val image = Uri.parse(imageUri)?.path ?: let {
            return Result.failure(IllegalArgumentException("Invalid imageUri"))
        }
        val file = File(image)

        return noAuthClient.put(preSignedUrl) {
            setBody(file.readBytes())
        }.convert(errorMessageMapper::map)
    }
}
