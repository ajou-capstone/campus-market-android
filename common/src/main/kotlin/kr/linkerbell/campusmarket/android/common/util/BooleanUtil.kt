package kr.linkerbell.campusmarket.android.common.util

fun Boolean?.orFalse(): Boolean {
    return this ?: false
}

fun Boolean?.orTrue(): Boolean {
    return this ?: true
}
