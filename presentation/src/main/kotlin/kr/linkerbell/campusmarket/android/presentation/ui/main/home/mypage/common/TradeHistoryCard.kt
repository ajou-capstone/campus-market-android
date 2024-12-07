package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat
import kotlinx.datetime.LocalDateTime
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.RecentTrade
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.common.TradeItemStatus

@Composable
internal fun TradeHistoryCard(
    isAddReviewIconVisible: Boolean = true,
    recentTrade: RecentTrade,
    onClicked: () -> Unit,
    onAddReviewClicked: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
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
            Spacer(modifier = Modifier.padding(4.dp))
            Column(
                modifier = Modifier.height(80.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = recentTrade.title,
                        style = Headline3,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    if (recentTrade.isSold) {


                        ReviewStatusIcon(
                            isVisible = isAddReviewIconVisible,
                            isReviewed = recentTrade.isReviewed,
                            navigateToReview = {
                                onAddReviewClicked()
                            }
                        )
                    }
                }
                Text(
                    text = recentTrade.nickname,
                    style = Body1,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TradeItemStatus(isSold = recentTrade.isSold)
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = "${DecimalFormat("#,###").format(recentTrade.price)} 원",
                            style = Body1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Text(
                        text = "작성 일자 : ${recentTrade.createdAt.date}",
                        style = Caption2,
                        color = Black
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = Gray200,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun ReviewStatusIcon(
    isVisible: Boolean,
    isReviewed: Boolean,
    navigateToReview: () -> Unit,
) {
    if (isVisible) {
        if (isReviewed) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(Space24)
                        .padding(horizontal = 4.dp),
                    painter = painterResource(R.drawable.ic_check_line),
                    contentDescription = null,
                    tint = Blue400
                )
                Text(
                    text = "리뷰 완료",
                    style = Caption2,
                    color = Gray600,
                    modifier = Modifier.padding(4.dp)
                )
            }
        } else {
            Row(
                modifier = Modifier.clickable {
                    navigateToReview()
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(Space24)
                        .padding(horizontal = 4.dp),
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                    tint = Blue400
                )
                Text(
                    text = "리뷰 작성하기",
                    style = Caption2,
                    color = Gray900,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TradeHistoryCardPreview() {
    TradeHistoryCard(
        isAddReviewIconVisible = true,
        RecentTrade(
            itemId = 123L,
            title = "Example Title",
            userId = 0L,
            nickname = "authorNickname",
            price = 5000,
            thumbnail = "",
            isSold = true,
            createdAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
            modifiedAt = LocalDateTime(2000, 1, 1, 0, 0, 0),
            isReviewed = false
        ),
        onClicked = {},
        onAddReviewClicked = {}
    )
}
