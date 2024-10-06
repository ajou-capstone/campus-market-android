package kr.linkerbell.campusmarket.android.common.util

fun Char?.orEmpty(): Char {
    return this ?: Char.MIN_VALUE
}
