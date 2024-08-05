package kr.linkerbell.boardlink.android.common.util

fun Char?.orEmpty(): Char {
    return this ?: Char.MIN_VALUE
}
