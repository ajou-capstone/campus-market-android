package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile.recent.review

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview

@Immutable
data class RecentReviewData(
    val recentReviews: LazyPagingItems<UserReview>
)
