package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue100
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.isEmpty
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_review.RecentReviewConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_trade.RecentTradeConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.info.TradeInfoConstant
import timber.log.Timber

@Composable
fun UserProfileScreen(
    navController: NavController,
    argument: UserProfileArgument,
    data: UserProfileData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val userprofile = data.userProfile

    var isNewScreenLoadingAvailable by remember { mutableStateOf(false) }

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
                .padding(8.dp)
                .constrainAs(contents) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            UserProfileInfo(userProfile = userprofile)
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray900,
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 16.dp)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                                navController.navigate(newRoute)
                            }
                        }
                    )
                }
                Column {
                    val recentTrades = data.recentTrades
                    Timber.tag("siri22").d("recentTrades : ${recentTrades.itemCount}")
                    if (recentTrades.isEmpty()) {
                        Text(
                            text = "아직 판매중인 물건이 없어요",
                            style = Caption2,
                            color = Gray600,
                            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                count = recentTrades.itemCount,
                                key = { index -> recentTrades[index]?.id ?: -1 }
                            ) { index ->
                                val trade = recentTrades[index] ?: return@items
                                TradeHistoryCard(
                                    recentTrade = trade,
                                    onClicked = {
                                        val tradeInfoRoute = makeRoute(
                                            route = TradeInfoConstant.ROUTE,
                                            arguments = mapOf(
                                                TradeInfoConstant.ROUTE_ARGUMENT_ITEM_ID
                                                        to trade.id.toString()
                                            )
                                        )
                                        navController.navigate(tradeInfoRoute)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                Column {
                    val recentReview = data.recentReviews
                    if (recentReview.isEmpty()) {
                        Text(
                            text = "아직 작성된 리뷰가 없어요",
                            style = Caption2,
                            color = Gray600,
                            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                count = recentReview.itemCount,
                                key = { index ->
                                    ("${recentReview[index]?.userId ?: -1}_${
                                        recentReview[index]?.createdAt?.date ?: Clock.System.now()
                                            .toLocalDateTime(TimeZone.currentSystemDefault())
                                    }").hashCode()
                                }
                            ) { index ->
                                val review = recentReview[index] ?: return@items
                                ReviewCard(review)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
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
private fun TradeHistoryCard(
    recentTrade: RecentTrade,
    onClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Gray200)
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClicked()
            }
    ) {
        PostImage(
            data = recentTrade.thumbnail,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = recentTrade.title,
                style = Headline2,
                color = Black,
            )
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            Text(
                text = "${recentTrade.price} 원",
                style = Headline3,
                color = Black,
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            TradeItemStatus(recentTrade.isSold)
        }
    }
}

@Composable
private fun TradeItemStatus(isSold: Boolean) {
    val backgroundColor = if (isSold) LightGray else Blue100
    val text = if (isSold) "거래 완료" else "거래 가능"

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = Body1,
            color = White,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun ReviewCard(review: UserReview) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gray200)
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
private fun RatingStars(
    rating: Int = 0,
    starSize: Dp = 20.dp,
    interval: Dp = 4.dp
) {
    val adjustedRating = rating.coerceIn(0, 10)

    @Composable
    fun StarImage(resourceId: Int, size: Dp) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Blue400),
            modifier = Modifier.size(size)
        )
    }

    Row {
        repeat(5) { index ->
            val starPoints = (index + 1) * 2
            val resource = when {
                adjustedRating >= starPoints -> R.drawable.filled_star
                adjustedRating >= starPoints - 1 -> R.drawable.half_filled_star
                else -> R.drawable.empty_star
            }
            StarImage(resource, starSize)
            Spacer(modifier = Modifier.padding(end = interval))
        }
    }
}

@Composable
private fun UserProfileInfo(
    userProfile: UserProfile
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
                style = Headline1,
                color = Black,
            )
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
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
                    text = " (${userProfile.rating}/10)",
                    style = Body1,
                    color = Black,
                )
            }
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
                rating = 4.4
            ),
            recentReviews = MutableStateFlow(
                PagingData.from(
                    listOf(
                        UserReview(
                            userId = 0L,
                            nickname = "reviewer_1",
                            profileImage = "",
                            description = "좋아요",
                            rating = 7,
                            createdAt = LocalDateTime(2024, 11, 22, 15, 30, 0)
                        ),
                        UserReview(
                            userId = 0L,
                            nickname = "reviewer_2",
                            profileImage = "",
                            description = "아주 좋아요",
                            rating = 10,
                            createdAt = LocalDateTime(2024, 11, 22, 15, 30, 0)
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            recentTrades = MutableStateFlow(
                PagingData.from(
                    listOf(
                        RecentTrade(
                            id = 1L,
                            title = "Used Laptop",
                            price = 150000,
                            thumbnail = "https://example.com/image1.jpg",
                            isSold = false
                        ),
                        RecentTrade(
                            id = 2L,
                            title = "Antique Vase",
                            price = 20000,
                            thumbnail = "https://example.com/image2.jpg",
                            isSold = true
                        )
                    )
                )
            ).collectAsLazyPagingItems()
        )
    )
}
