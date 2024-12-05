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
            KEY_BLACKLIST_USER -> R.string.error_blacklist_user
            KEY_USER_NOT_FOUND -> R.string.error_user_not_found
            KEY_ITEM_NOT_FOUND -> R.string.error_item_not_found
            KEY_SCHOOL_EMAIL_NOT_FOUND -> R.string.error_school_email_not_found
            KEY_CAMPUS_NOT_FOUND -> R.string.error_campus_not_found
            KEY_INVALID_SCHOOL_EMAIL -> R.string.error_invalid_school_email
            KEY_INVALID_CATEGORY -> R.string.error_invalid_category
            KEY_INVALID_PRICE -> R.string.error_invalid_price
            KEY_INVALID_SORT -> R.string.error_invalid_sort
            KEY_INVALID_VERIFICATION_CODE -> R.string.error_invalid_verification_code
            KEY_INVALID_ITEM_PHOTOS_COUNT -> R.string.error_invalid_item_photos_count
            KEY_INVALID_TITLE -> R.string.error_invalid_title
            KEY_INVALID_DESCRIPTION -> R.string.error_invalid_description
            KEY_INVALID_THUMBNAIL -> R.string.error_invalid_thumbnail
            KEY_INVALID_ITEM_PHOTOS -> R.string.error_invalid_item_photos
            KEY_DUPLICATE_ITEM_PHOTOS -> R.string.error_duplicate_item_photos
            KEY_INVALID_ITEM_ID -> R.string.error_invalid_item_id
            KEY_DELETED_ITEM_ID -> R.string.error_deleted_item_id
            KEY_NOT_MATCH_USER_UNIVERSITY_WITH_ITEM_UNIVERSITY -> R.string.error_not_match_user_university_with_item_university
            KEY_USER_TERMS_INFO_NOT_FOUND -> R.string.error_user_terms_info_not_found
            KEY_DUPLICATE_SCHOOL_EMAIL -> R.string.error_duplicate_school_email
            KEY_NOT_MATCH_USER_ID_WITH_ITEM_USER_ID -> R.string.error_not_match_user_id_with_item_user_id
            KEY_INVALID_FILE_NAME -> R.string.error_invalid_file_name
            KEY_CHATROOM_NOT_FOUND -> R.string.error_chatroom_not_found
            KEY_MESSAGE_NOT_FOUND -> R.string.error_message_not_found
            KEY_INVALID_ITEM_STATUS -> R.string.error_invalid_item_status
            KEY_INVALID_ITEM_BUYER -> R.string.error_invalid_item_buyer
            KEY_DO_NOT_ROLL_BACK_ITEM_STATUS_FOR_SALE -> R.string.error_do_not_roll_back_item_status_for_sale
            KEY_ALREADY_SOLD_OUT_ITEM -> R.string.error_already_sold_out_item
            KEY_NOT_MATCH_USER_CAMPUS -> R.string.error_not_match_user_campus
            KEY_INVALID_CONTENT_TYPE -> R.string.error_invalid_content_type
            KEY_DUPLICATE_CHATROOM -> R.string.error_duplicate_chatroom
            KEY_EMPTY_ITEM_THUMBNAIL -> R.string.error_empty_item_thumbnail
            KEY_NOT_ADMINISTRATOR -> R.string.error_not_administrator
            KEY_NOT_REPORT_OWN -> R.string.error_not_report_own
            KEY_NOT_AUTHORIZED -> R.string.error_not_authorized
            KEY_INVALID_REVIEW_DESCRIPTION -> R.string.error_invalid_review_description
            KEY_INVALID_REVIEW_RATING -> R.string.error_invalid_review_rating
            KEY_INVALID_PAGEABLE_PAGE -> R.string.error_invalid_pageable_page
            KEY_INVALID_PAGEABLE_SIZE -> R.string.error_invalid_pageable_size
            KEY_INVALID_NOTIFICATION_ID -> R.string.error_invalid_notification_id
            KEY_NOT_MATCH_USER_ID_WITH_NOTIFICATION_USER_ID -> R.string.error_not_match_user_id_with_notification_user_id
            KEY_INVALID_QUESTION_DESCRIPTION -> R.string.error_invalid_question_description
            KEY_INVALID_QUESTION_TITLE -> R.string.error_invalid_question_title
            KEY_INVALID_KEYWORD_NAME -> R.string.error_invalid_keyword_name
            KEY_INVALID_KEYWORD_COUNT -> R.string.error_invalid_keyword_count
            KEY_DUPLICATE_KEYWORD_NAME -> R.string.error_duplicate_keyword_name
            KEY_INVALID_KEYWORD_ID -> R.string.error_invalid_keyword_id
            KEY_NOT_MATCH_USER_AND_KEYWORD_USER -> R.string.error_not_match_user_and_keyword_user
            KEY_ITEM_REPORT_NOT_FOUND -> R.string.error_item_report_not_found
            KEY_USER_REPORT_NOT_FOUND -> R.string.error_user_report_not_found
            KEY_REQUIRE_DELETE_OR_NOT -> R.string.error_require_delete_or_not
            KEY_NOT_NULL_USER_REPORT_SUSPENDED_OR_NOT -> R.string.error_not_null_user_report_suspended_or_not
            KEY_QA_NOT_FOUND -> R.string.error_qa_not_found
            KEY_INVALID_QA_ANSWER -> R.string.error_invalid_qa_answer
            KEY_NOT_MATCH_QA_USER -> R.string.error_not_match_qa_user
            KEY_INVALID_DEAL_TYPE -> R.string.error_invalid_deal_type
            KEY_NOT_MATCH_LOGIN_USER_AND_REQUESTED_USER -> R.string.error_not_match_login_user_and_requested_user

            KEY_FCM_INVALID_ARGUMENT -> R.string.error_fcm_invalid_argument
            KEY_FCM_UNREGISTERED -> R.string.error_fcm_unregistered

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
        const val KEY_BLACKLIST_USER = "4008"
        const val KEY_USER_NOT_FOUND = "4011"
        const val KEY_ITEM_NOT_FOUND = "4012"
        const val KEY_SCHOOL_EMAIL_NOT_FOUND = "4013"
        const val KEY_CAMPUS_NOT_FOUND = "4014"
        const val KEY_INVALID_SCHOOL_EMAIL = "4015"
        const val KEY_INVALID_CATEGORY = "4016"
        const val KEY_INVALID_PRICE = "4017"
        const val KEY_INVALID_SORT = "4018"
        const val KEY_INVALID_VERIFICATION_CODE = "4019"
        const val KEY_INVALID_ITEM_PHOTOS_COUNT = "4020"
        const val KEY_INVALID_TITLE = "4021"
        const val KEY_INVALID_DESCRIPTION = "4022"
        const val KEY_INVALID_THUMBNAIL = "4023"
        const val KEY_INVALID_ITEM_PHOTOS = "4024"
        const val KEY_DUPLICATE_ITEM_PHOTOS = "4025"
        const val KEY_INVALID_ITEM_ID = "4026"
        const val KEY_DELETED_ITEM_ID = "4027"
        const val KEY_NOT_MATCH_USER_UNIVERSITY_WITH_ITEM_UNIVERSITY = "4028"
        const val KEY_USER_TERMS_INFO_NOT_FOUND = "4029"
        const val KEY_DUPLICATE_SCHOOL_EMAIL = "4030"
        const val KEY_NOT_MATCH_USER_ID_WITH_ITEM_USER_ID = "4031"
        const val KEY_INVALID_FILE_NAME = "4032"
        const val KEY_CHATROOM_NOT_FOUND = "4033"
        const val KEY_MESSAGE_NOT_FOUND = "4034"
        const val KEY_INVALID_ITEM_STATUS = "4035"
        const val KEY_INVALID_ITEM_BUYER = "4036"
        const val KEY_DO_NOT_ROLL_BACK_ITEM_STATUS_FOR_SALE = "4037"
        const val KEY_ALREADY_SOLD_OUT_ITEM = "4038"
        const val KEY_NOT_MATCH_USER_CAMPUS = "4039"
        const val KEY_INVALID_CONTENT_TYPE = "4040"
        const val KEY_DUPLICATE_CHATROOM = "4041"
        const val KEY_EMPTY_ITEM_THUMBNAIL = "4042"
        const val KEY_NOT_ADMINISTRATOR = "4043"
        const val KEY_NOT_REPORT_OWN = "4044"
        const val KEY_NOT_AUTHORIZED = "4045"
        const val KEY_INVALID_REVIEW_DESCRIPTION = "4046"
        const val KEY_INVALID_REVIEW_RATING = "4047"
        const val KEY_INVALID_PAGEABLE_PAGE = "4048"
        const val KEY_INVALID_PAGEABLE_SIZE = "4049"
        const val KEY_INVALID_NOTIFICATION_ID = "4050"
        const val KEY_NOT_MATCH_USER_ID_WITH_NOTIFICATION_USER_ID = "4051"
        const val KEY_INVALID_QUESTION_DESCRIPTION = "4052"
        const val KEY_INVALID_QUESTION_TITLE = "4053"
        const val KEY_INVALID_KEYWORD_NAME = "4054"
        const val KEY_INVALID_KEYWORD_COUNT = "4055"
        const val KEY_DUPLICATE_KEYWORD_NAME = "4056"
        const val KEY_INVALID_KEYWORD_ID = "4057"
        const val KEY_NOT_MATCH_USER_AND_KEYWORD_USER = "4058"
        const val KEY_ITEM_REPORT_NOT_FOUND = "4059"
        const val KEY_USER_REPORT_NOT_FOUND = "4060"
        const val KEY_REQUIRE_DELETE_OR_NOT = "4061"
        const val KEY_NOT_NULL_USER_REPORT_SUSPENDED_OR_NOT = "4062"
        const val KEY_QA_NOT_FOUND = "4063"
        const val KEY_INVALID_QA_ANSWER = "4064"
        const val KEY_NOT_MATCH_QA_USER = "4065"
        const val KEY_INVALID_DEAL_TYPE = "4066"
        const val KEY_NOT_MATCH_LOGIN_USER_AND_REQUESTED_USER = "4067"

        const val KEY_FCM_INVALID_ARGUMENT = "6001"
        const val KEY_FCM_UNREGISTERED = "6002"
    }
}
