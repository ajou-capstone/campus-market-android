package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Profile

@Immutable
data class MyPageData(
    val profile: Profile,
    val fcmToken: String
)
