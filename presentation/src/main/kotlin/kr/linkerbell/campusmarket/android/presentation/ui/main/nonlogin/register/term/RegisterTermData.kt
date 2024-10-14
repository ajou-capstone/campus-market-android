package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.term

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.term.Term

@Immutable
data class RegisterTermData(
    val termList: List<Term>
)
