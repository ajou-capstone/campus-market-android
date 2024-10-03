package kr.linkerbell.boardlink.android.presentation.ui.main.testpage

import androidx.compose.runtime.Immutable
import kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile.RandomUserProfile

@Immutable
data class TestPageData(
    val randomUserProfile: RandomUserProfile?,
    val currentSemester: String = "2024-2"
)
