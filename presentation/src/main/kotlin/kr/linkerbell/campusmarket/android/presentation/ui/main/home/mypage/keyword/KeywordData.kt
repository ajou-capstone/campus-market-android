package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.keyword

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.Keyword

@Immutable
data class KeywordData(
    val myKeywords: List<Keyword>
)
