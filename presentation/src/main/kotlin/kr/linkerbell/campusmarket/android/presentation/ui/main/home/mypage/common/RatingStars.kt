package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400

@Composable
internal fun RatingStars(
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
private fun RatingStarsPreview() {
    RatingStars(
        rating = 5,
        starSize = 20.dp
    )
}
