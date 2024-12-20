package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.edit.campus

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Campus

@Immutable
data class ChangeCampusData(
    val campusList: List<Campus>
)
