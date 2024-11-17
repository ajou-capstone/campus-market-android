package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_review

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile

@Immutable
data class RecentReviewData(
    val recentReviews: LazyPagingItems<UserReview>
)
