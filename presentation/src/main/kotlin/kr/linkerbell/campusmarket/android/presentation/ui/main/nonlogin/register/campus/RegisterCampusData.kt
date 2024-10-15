package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.campus

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Campus

@Immutable
data class RegisterCampusData(
    val campusList: List<Campus>
)
