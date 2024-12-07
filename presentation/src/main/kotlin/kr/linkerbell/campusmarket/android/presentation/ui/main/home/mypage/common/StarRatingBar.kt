package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400

@Composable
fun StarRatingBar(
    rating: Int,
    width: Dp,
    onRatingChanged: (Int) -> Unit
) {
    val adjustedRating = remember { mutableIntStateOf(rating) }

    Box(
        modifier = Modifier.width(width),
        contentAlignment = Alignment.Center
    ) {
        Slider(
            value = adjustedRating.intValue.toFloat(),
            onValueChange = { newValue ->
                adjustedRating.intValue = newValue.roundToInt()
                onRatingChanged(adjustedRating.intValue)
            },
            valueRange = 0f..10f,
            steps = 10,
            modifier = Modifier
                .width(width * 2.0f)
                .alpha(0f)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            for (i in 1..5) {
                when {
                    adjustedRating.intValue >= i * 2 -> {
                        Image(
                            painter = painterResource(id = R.drawable.filled_star),
                            contentDescription = "Filled Star",
                            colorFilter = ColorFilter.tint(Blue400),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    adjustedRating.intValue >= (i * 2) - 1 -> {
                        Image(
                            painter = painterResource(id = R.drawable.half_filled_star),
                            contentDescription = "Half Filled Star",
                            colorFilter = ColorFilter.tint(Blue400),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    else -> {
                        Image(
                            painter = painterResource(id = R.drawable.empty_star),
                            contentDescription = "Empty Star",
                            colorFilter = ColorFilter.tint(Blue400),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
