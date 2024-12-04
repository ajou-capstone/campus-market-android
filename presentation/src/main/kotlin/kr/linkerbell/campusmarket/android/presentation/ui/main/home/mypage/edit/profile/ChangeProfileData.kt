package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.edit.profile

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile

@Immutable
data class ChangeProfileData(
    val myProfile: MyProfile
)
