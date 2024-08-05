package kr.linkerbell.boardlink.android.presentation.common.view.confirm

import androidx.compose.runtime.Immutable

@Immutable
data class ConfirmButtonProperties(
    val size: ConfirmButtonSize,
    val type: ConfirmButtonType
)
