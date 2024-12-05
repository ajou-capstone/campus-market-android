package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common.ReviewCard

@Composable
fun MyRecentReviewScreen(
    navController: NavController,
    argument: MyRecentReviewArgument,
    data: MyRecentReviewData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val reviewsToMe by remember { mutableStateOf(data.reviewsToMe) }
    val myReviews by remember { mutableStateOf(data.myReviews) }

    var selectedTab by remember { mutableIntStateOf(0) }

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
                text = "최근 리뷰 목록",
                style = Headline2
            )
        }
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxHeight()
                .constrainAs(contents) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            Row {
                MyRecentReviewListTab(
                    text = "내게 달린 리뷰",
                    isSelected = (selectedTab == 0),
                    onClicked = {
                        selectedTab = 0
                        argument.intent(MyRecentReviewIntent.RefreshReviewData)
                    },
                    modifier = Modifier.weight(1f)
                )
                MyRecentReviewListTab(
                    text = "내가 쓴 리뷰",
                    isSelected = (selectedTab == 1),
                    onClicked = {
                        selectedTab = 1
                        argument.intent(MyRecentReviewIntent.RefreshReviewData)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            val visibleReviewList = when (selectedTab) {
                0 -> reviewsToMe
                1 -> myReviews
                else -> myReviews
            }
            if (visibleReviewList.itemCount == 0) {
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
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
            ) {
                items(
                    count = visibleReviewList.itemCount,
                    key = { index ->
                        visibleReviewList[index]?.reviewId ?: -1L
                    }
                ) { index ->
                    val review = visibleReviewList[index] ?: return@items
                    ReviewCard(review)
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MyRecentReviewListTab(
    text: String,
    isSelected: Boolean,
    onClicked: () -> Unit,
    modifier: Modifier
) {
    val textStyle = if (isSelected) Headline1 else Headline2

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClicked()
            }
    ) {
        Text(
            text = text,
            style = textStyle,
            color = Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (isSelected) {
            HorizontalDivider(
                thickness = 2.dp,
                color = Gray900,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Preview
@Composable
private fun RecentReviewScreenPreview() {
    MyRecentReviewScreen(
        navController = rememberNavController(),
        argument = MyRecentReviewArgument(
            state = MyRecentReviewState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = MyRecentReviewData(
            reviewsToMe = MutableStateFlow(
                PagingData.from(
                    listOf(
                        UserReview(
                            reviewId = 0L,
                            nickname = "reviewer_1",
                            profileImage = "",
                            description = "좋아요",
                            rating = 7,
                            createdAt = LocalDateTime(2024, 11, 22, 15, 30, 0)
                        ),
                        UserReview(
                            reviewId = 1L,
                            nickname = "reviewer_2",
                            profileImage = "",
                            description = "아주 좋아요",
                            rating = 10,
                            createdAt = LocalDateTime(2024, 10, 22, 15, 30, 0)
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            myReviews = MutableStateFlow(
                PagingData.from(
                    listOf(
                        UserReview(
                            reviewId = 0L,
                            nickname = "reviewer_1",
                            profileImage = "",
                            description = "좋아요",
                            rating = 7,
                            createdAt = LocalDateTime(2024, 11, 22, 15, 30, 0)
                        ),
                        UserReview(
                            reviewId = 1L,
                            nickname = "reviewer_2",
                            profileImage = "",
                            description = "아주 좋아요",
                            rating = 10,
                            createdAt = LocalDateTime(2024, 10, 22, 15, 30, 0)
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
        )
    )
}
