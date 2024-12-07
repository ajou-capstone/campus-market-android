package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.plus
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space52
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.isEmpty
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common.RatingStars
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common.TradeHistoryCard
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.rating.RatingConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.user.UserReportConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile.recent.review.RecentReviewConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.userprofile.recent.trade.RecentTradeConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.common.table.ScheduleTable
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.common.table.ScheduleTableData
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.TradeInfoConstant

@Composable
fun UserProfileScreen(
    navController: NavController,
    argument: UserProfileArgument,
    data: UserProfileData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val userprofile = data.userProfile
    val recentReview = data.recentReviews
    val recentTrades = data.recentTrades

    val startTime = LocalTime(9, 0)
    val endTime = LocalTime(22, 0)

    var isNewScreenLoadingAvailable by remember { mutableStateOf(false) }

    fun navigateToUserReportScreen(userId: Long) {
        val newRoute = makeRoute(
            route = UserReportConstant.ROUTE,
            arguments = mapOf(
                UserReportConstant.ROUTE_ARGUMENT_USER_ID to userId.toString()
            )
        )
        navController.navigate(newRoute)

    }

    ConstraintLayout(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
    ) {
        val (topBar, contents) = createRefs()
        Row(
            modifier = Modifier
                .height(Space56)
                .fillMaxWidth()
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            RippleBox(
                modifier = Modifier
                    .padding(4.dp),
                onClick = {
                    navController.safeNavigateUp()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = "Navigate Up Button",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(horizontal = 8.dp)
                )
            }
            Text(
                text = "작성자 정보",
                style = Headline2
            )
        }
        Column(
            modifier = Modifier
                .constrainAs(contents) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            UserProfileInfo(
                userProfile = userprofile,
                onReportUserClicked = { navigateToUserReportScreen(userprofile.id) }
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray900,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
            val parentScrollState = rememberScrollState()
            val childLazyListState = rememberLazyListState()
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(parentScrollState)
                    .nestedScroll(rememberNestedScrollInteropConnection())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "최근 판매",
                        style = Headline1,
                        color = Black,
                    )
                    Text(
                        text = "더보기",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.clickable {
                            if (isNewScreenLoadingAvailable) {
                                isNewScreenLoadingAvailable = false
                                val newRoute = makeRoute(
                                    route = RecentTradeConstant.ROUTE,
                                    arguments = mapOf(
                                        RecentTradeConstant.ROUTE_ARGUMENT_USER_ID to data.userProfile.id,
                                    )
                                )
                                navController.safeNavigate(newRoute)
                            }
                        }
                    )
                }
                if (recentTrades.isEmpty()) {
                    Text(
                        text = "아직 판매중인 물건이 없어요",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                    )
                } else {
                    LazyColumn(
                        state = childLazyListState,
                        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(
                            count = minOf(recentTrades.itemCount, 3),
                            key = { index -> recentTrades[index]?.itemId ?: -1 }
                        ) { index ->
                            val trade = recentTrades[index] ?: return@items
                            TradeHistoryCard(
                                isAddReviewIconVisible = false,
                                isOwnerOfThisTrade = false,
                                recentTrade = trade,
                                onClicked = {
                                    val tradeInfoRoute = makeRoute(
                                        route = TradeInfoConstant.ROUTE,
                                        arguments = mapOf(
                                            TradeInfoConstant.ROUTE_ARGUMENT_ITEM_ID
                                                    to trade.itemId.toString()
                                        )
                                    )
                                    navController.safeNavigate(tradeInfoRoute)
                                },
                                onAddReviewClicked = { targetUserId, itemId ->
                                    val reviewInfoRoute = makeRoute(
                                        route = TradeInfoConstant.ROUTE,
                                        arguments = mapOf(
                                            RatingConstant.ROUTE_ARGUMENT_USER_ID to targetUserId,
                                            RatingConstant.ROUTE_ARGUMENT_ITEM_ID to itemId
                                        )
                                    )
                                    navController.safeNavigate(reviewInfoRoute)
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "최근 평가",
                        style = Headline1,
                        color = Black,
                    )
                    Text(
                        text = "더보기",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.clickable {
                            if (isNewScreenLoadingAvailable) {
                                isNewScreenLoadingAvailable = false
                                val newRoute = makeRoute(
                                    route = RecentReviewConstant.ROUTE,
                                    arguments = mapOf(
                                        RecentReviewConstant.ROUTE_ARGUMENT_USER_ID to data.userProfile.id,
                                    )
                                )
                                navController.safeNavigate(newRoute)
                            }
                        }
                    )
                }

                if (recentReview.isEmpty()) {
                    Text(
                        text = "아직 작성된 리뷰가 없어요",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                    )
                } else {
                    LazyColumn(
                        state = childLazyListState,
                        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(
                            count = minOf(recentReview.itemCount, 3),
                            key = { index ->
                                recentReview[index]?.reviewId ?: -1L
                            }
                        ) { index ->
                            val review = recentReview[index] ?: return@items
                            ReviewCard(review)
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Gray200,
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "시간표",
                        style = Headline1,
                        color = Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    ScheduleTable(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(Space52 * (endTime.hour - startTime.hour + 1))
                            .fillMaxWidth(),
                        dataList = listOf(
                            ScheduleTableData(
                                color = Blue200,
                                scheduleList = data.userSchedule
                            )
                        ),
                        startTime = startTime,
                        endTime = endTime
                    )

                }
            }
        }
    }

    LaunchedEffectWithLifecycle(coroutineContext) {
        argument.intent(UserProfileIntent.RefreshUserProfile)
        isNewScreenLoadingAvailable = true
    }
}

@Composable
private fun ReviewCard(review: UserReview) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row {
            PostImage(
                data = review.profileImage,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        color = White,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        1.dp,
                        Gray900,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = review.nickname,
                        style = Headline2,
                        color = Black
                    )
                    Text(
                        text = review.createdAt.date.toString(),
                        style = Body2,
                        color = Gray600,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RatingStars(
                        rating = review.rating,
                        starSize = 14.dp,
                        interval = 2.dp
                    )
                    Text(
                        text = "(${review.rating})",
                        style = Body2,
                        color = Black,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = review.description,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                style = Body1,
                color = Gray900,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
private fun UserProfileInfo(
    userProfile: UserProfile,
    onReportUserClicked: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(150.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(White),
            elevation = cardElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
            border = BorderStroke(3.dp, Blue400)
        ) {
            PostImage(
                data = userProfile.profileImage,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = userProfile.nickname,
                style = Headline0,
                color = Black,
            )
            Spacer(modifier = Modifier.padding(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.filled_star),
                    contentDescription = null,
                    tint = Blue400
                )
                Text(
                    text = " (${String.format("%.1f", userProfile.rating)})",
                    style = Body1,
                    color = Black,
                )
            }
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            Text(
                text = "이 사용자 신고하기",
                style = Caption2,
                color = Gray600,
                modifier = Modifier.clickable {
                    onReportUserClicked()
                }
            )
        }
    }
}

@Preview
@Composable
private fun OtherUserProfileScreenPreview() {
    UserProfileScreen(
        navController = rememberNavController(),
        argument = UserProfileArgument(
            state = UserProfileState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = UserProfileData(
            UserProfile(
                id = 959L,
                nickname = "user3205",
                profileImage = "https://picsum.photos/200",
                rating = 4.4,
                isDeleted = false
            ),
            recentReviews = MutableStateFlow(
                PagingData.empty<UserReview>()
//                PagingData.from(
//                    listOf(
//                        UserReview(
//                            reviewId = 0L,
//                            nickname = "reviewer_1",
//                            profileImage = "",
//                            description = "좋아요",
//                            rating = 7,
//                            createdAt = LocalDateTime(2024, 11, 22, 15, 30, 0)
//                        ),
//                        UserReview(
//                            reviewId = 1L,
//                            nickname = "reviewer_2",
//                            profileImage = "",
//                            description = "아주 좋아요",
//                            rating = 10,
//                            createdAt = LocalDateTime(2024, 10, 22, 15, 30, 0)
//                        ),
//                        UserReview(
//                            reviewId = 2L,
//                            nickname = "reviewer_1",
//                            profileImage = "",
//                            description = "좋아요",
//                            rating = 7,
//                            createdAt = LocalDateTime(2024, 11, 22, 15, 30, 0)
//                        )
//                    )
//                )
            ).collectAsLazyPagingItems(),
            recentTrades = MutableStateFlow(
//                PagingData.empty<RecentTrade>()
                PagingData.from(
                    listOf(
                        RecentTrade(
                            itemId = 1L,
                            title = "Used Laptop",
                            userId = 1L,
                            buyerId = 2L,
                            nickname = "author_01",
                            price = 150000,
                            thumbnail = "https://example.com/image1.jpg",
                            isSold = false,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = true
                        ),
                        RecentTrade(
                            itemId = 2L,
                            title = "Antique Vase",
                            userId = 2L,
                            buyerId = 1L,
                            nickname = "author_22",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = false
                        ),
                        RecentTrade(
                            itemId = 3L,
                            title = "Antique Vase",
                            userId = 3L,
                            buyerId = 5L,
                            nickname = "author_333",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true,
                            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
                            isReviewed = false
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            userSchedule = listOf(
                Schedule(
                    dayOfWeek = 1,
                    startTime = LocalTime(9, 0),
                    endTime = LocalTime(12, 0)
                ),
                Schedule(
                    dayOfWeek = 1,
                    startTime = LocalTime(15, 0),
                    endTime = LocalTime(16, 30)
                ),
            )
        )
    )
}
