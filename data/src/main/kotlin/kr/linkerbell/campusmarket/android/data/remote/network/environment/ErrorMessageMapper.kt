package kr.linkerbell.campusmarket.android.data.remote.network.environment

import android.content.Context
import kr.linkerbell.campusmarket.android.data.R
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.UndefinedKeyException
import timber.log.Timber

class ErrorMessageMapper(
    private val context: Context
) {
    fun map(
        key: String
    ): String {
        val id = when (key) {
            KEY_INTERNAL_SERVER_ERROR -> R.string.error_internal_server_error

            KEY_INVALID_GOOGLE_TOKEN -> R.string.error_invalid_google_token
            KEY_INVALID_JWT -> R.string.error_invalid_jwt
            KEY_EXPIRED_JWT -> R.string.error_expired_jwt
            KEY_LOGOUT_JWT -> R.string.error_logout_jwt
            KEY_UNVERIFIED_GOOGLE_TOKEN -> R.string.error_unverified_google_token
            KEY_NOT_VERIFIED_EMAIL -> R.string.error_not_verified_email
            KEY_JWT_IS_NULL -> R.string.error_jwt_is_null
            KEY_USER_NOT_FOUND -> R.string.error_user_not_found
            KEY_ITEM_NOT_FOUND -> R.string.error_item_not_found
            KEY_SCHOOL_EMAIL_NOT_FOUND -> R.string.error_school_email_not_found
            KEY_CAMPUS_NOT_FOUND -> R.string.error_campus_not_found
            KEY_INVALID_SCHOOL_EMAIL -> R.string.error_invalid_school_email
            KEY_INVALID_CATEGORY -> R.string.error_invalid_category
            KEY_INVALID_PRICE -> R.string.error_invalid_price
            KEY_INVALID_SORT -> R.string.error_invalid_sort
            KEY_INVALID_VERIFICATION_CODE -> R.string.error_invalid_verification_code

            else -> {
                Timber.e(UndefinedKeyException("Undefined error key: $key"))
                R.string.error_unknown
            }
        }

        return context.getString(id)
    }

    companion object {
        const val KEY_INTERNAL_SERVER_ERROR = "5000"

        const val KEY_INVALID_GOOGLE_TOKEN = "4001"
        const val KEY_INVALID_JWT = "4002"
        const val KEY_EXPIRED_JWT = "4003"
        const val KEY_LOGOUT_JWT = "4004"
        const val KEY_UNVERIFIED_GOOGLE_TOKEN = "4005"
        const val KEY_NOT_VERIFIED_EMAIL = "4006"
        const val KEY_JWT_IS_NULL = "4007"
        const val KEY_USER_NOT_FOUND = "4011"
        const val KEY_ITEM_NOT_FOUND = "4012"
        const val KEY_SCHOOL_EMAIL_NOT_FOUND = "4013"
        const val KEY_CAMPUS_NOT_FOUND = "4014"
        const val KEY_INVALID_SCHOOL_EMAIL = "4015"
        const val KEY_INVALID_CATEGORY = "4016"
        const val KEY_INVALID_PRICE = "4017"
        const val KEY_INVALID_SORT = "4018"
        const val KEY_INVALID_VERIFICATION_CODE = "4019"
    }
}
