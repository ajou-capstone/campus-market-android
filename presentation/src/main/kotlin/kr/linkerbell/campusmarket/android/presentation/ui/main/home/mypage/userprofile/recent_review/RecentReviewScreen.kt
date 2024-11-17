package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.userprofile.recent_review

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import kotlinx.datetime.LocalDateTime
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage

@Composable
fun RecentReviewScreen(
    navController: NavController,
    argument: RecentReviewArgument,
    data: RecentReviewData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val recentReviewList = data.recentReviews

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
                text = "최근 평가",
                style = Headline2
            )
        }
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .constrainAs(contents) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            if (recentReviewList.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "아직 작성된 리뷰가 없어요",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                }
            }
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)
            ) {
                items(
                    count = recentReviewList.itemCount,
                    key = { index ->
                        "${recentReviewList[index]?.userId ?: -1}_" +
                                "${recentReviewList[index]?.createdAt!!.date}".hashCode()
                    }
                ) { index ->
                    val review = recentReviewList[index] ?: return@items
                    ReviewCard(review)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
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

@Preview
@Composable
private fun RecentReviewScreenPreview() {
    RecentReviewScreen(
        navController = rememberNavController(),
        argument = RecentReviewArgument(
            state = RecentReviewState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = RecentReviewData(
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
        )
    )
}
