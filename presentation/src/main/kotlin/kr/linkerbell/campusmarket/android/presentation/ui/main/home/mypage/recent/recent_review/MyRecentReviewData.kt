package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_review

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview

@Immutable
data class MyRecentReviewData(
    val recentReviews: LazyPagingItems<UserReview>
)
