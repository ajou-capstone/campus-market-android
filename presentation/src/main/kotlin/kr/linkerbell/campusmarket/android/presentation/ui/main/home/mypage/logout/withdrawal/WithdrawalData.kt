package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.withdrawal

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile

@Immutable
data class WithdrawalData(
    val myProfile: MyProfile
)
