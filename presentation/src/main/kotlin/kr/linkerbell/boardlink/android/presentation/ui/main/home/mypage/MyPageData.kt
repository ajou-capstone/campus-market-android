package kr.linkerbell.boardlink.android.presentation.ui.main.home.mypage

import androidx.compose.runtime.Immutable
import kr.linkerbell.boardlink.android.domain.model.nonfeature.user.Profile

@Immutable
data class MyPageData(
    val profile: Profile
)
