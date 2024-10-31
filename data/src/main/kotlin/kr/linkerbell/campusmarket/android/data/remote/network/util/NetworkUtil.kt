package kr.linkerbell.campusmarket.android.data.remote.network.util

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.nonfeature.error.ErrorRes
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.BadRequestServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.InternalServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.InvalidStandardResponseException
import timber.log.Timber

val HttpResponse.isSuccessful: Boolean
    get() = status.value in 200..299

val HttpResponse.isBadRequest: Boolean
    get() = status.value in 400..499

val HttpResponse.isInternalServerError: Boolean
    get() = status.value in 500..599

inline fun <reified T : DataMapper<R>, R : Any> Result<T>.toDomain(): Result<R> {
    return map { it.toDomain() }
}

suspend inline fun <reified T : Any> HttpResponse.convert(
    noinline errorMessageMapper: (String) -> String
): Result<T> {
    return runCatching {
        if (isSuccessful) {
            return@runCatching body<T?>()
                ?: throw InvalidStandardResponseException("Response Empty Body")
        } else {
            throw this.toThrowable(errorMessageMapper)
        }
    }.onFailure { exception ->
        Timber.d(exception)
        Firebase.crashlytics.recordException(exception)
    }
}

suspend inline fun HttpResponse.toThrowable(
    noinline errorMessageMapper: (String) -> String
): Throwable {
    return runCatching {
        if (isInternalServerError) {
            return@runCatching InternalServerException(
                ErrorMessageMapper.KEY_INTERNAL_SERVER_ERROR,
                errorMessageMapper(ErrorMessageMapper.KEY_INTERNAL_SERVER_ERROR)
            )
        }

        return@runCatching this.body<ErrorRes?>()?.let { errorRes ->
            BadRequestServerException(errorRes.code, errorMessageMapper(errorRes.code))
        } ?: InvalidStandardResponseException("Response Empty Body")
    }.getOrElse { exception ->
        exception
    }
}

fun HttpRequestBuilder.parameterFiltered(key: String, value: Any?) {
    value?.toString()?.ifEmpty { null }?.let {
        url.parameters.append(key, it)
    }
}
