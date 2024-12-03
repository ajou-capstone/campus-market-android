package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserReview
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage

@Composable
internal fun ReviewCard(review: UserReview) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))

            .background(White)
    ) {
        Row {
            PostImage(
                data = review.profileImage,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
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
            )
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Gray200,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Preview
@Composable
private fun ReviewCardPreview() {
    ReviewCard(
        review = UserReview(
            userId = 0L,
            nickname = "reviewer_1",
            profileImage = "",
            description = "좋아요",
            rating = 7,
            createdAt = LocalDateTime(2024, 11, 22, 15, 30, 0)
        )
    )
}
