package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile

import androidx.compose.runtime.Immutable
import androidx.paging.compose.LazyPagingItems
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile

@Immutable
data class UserProfileData(
    val userProfile: UserProfile,
    val recentReviews: LazyPagingItems<UserReview>,
    val recentTrades: LazyPagingItems<RecentTrade>,
    val userSchedule: List<Schedule>
)
