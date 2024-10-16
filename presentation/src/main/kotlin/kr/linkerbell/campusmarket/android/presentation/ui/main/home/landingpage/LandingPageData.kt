package kr.linkerbell.campusmarket.android.presentation.ui.main.home.landingpage

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.item.SummarizedItem

@Immutable
data class LandingPageData(
    val latestSummarizedItemList: MutableList<SummarizedItem>
)
