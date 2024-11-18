package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.rating

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.roundToInt
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField

@Composable
fun RatingScreen() {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var userRating by remember { mutableFloatStateOf(0.0f) }
    var userDescription by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {},
    ) {
        Column(
            modifier = Modifier
                .size(height = screenHeight * 0.4f, width = screenWidth * 0.6f)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "거래는 어떠셨나요?",
                style = Headline2,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "상대방에 대한 리뷰를 남겨주세요",
                style = Body2,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = userRating.toString(),
                style = Headline1,
                modifier = Modifier.padding(8.dp)
            )
            StarRatingBar(
                rating = userRating,
                180.dp,
                onRatingChanged = { newRating ->
                    userRating = newRating
                }
            )
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
            ) {
                TypingTextField(
                    text = userDescription,
                    onValueChange = { userDescription = it },
                    modifier = Modifier.fillMaxHeight()
                )
            }

        }
    }
}


@Composable
private fun StarRatingBar(
    rating: Float,
    width: Dp,
    onRatingChanged: (Float) -> Unit
) {
    val adjustedRating = remember { mutableFloatStateOf(rating) }

    Box(
        modifier = Modifier.width(width),
        contentAlignment = Alignment.Center
    ) {
        Slider(
            value = adjustedRating.floatValue,
            onValueChange = { newValue ->
                adjustedRating.floatValue = (newValue * 2).roundToInt() / 2f
                onRatingChanged(adjustedRating.floatValue)
            },
            valueRange = 0f..5f,
            steps = 9,
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
                    adjustedRating.floatValue >= i -> {
                        Image(
                            painter = painterResource(id = R.drawable.filled_star),
                            contentDescription = "Filled Star",
                            colorFilter = ColorFilter.tint(Blue400),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    adjustedRating.floatValue >= i - 0.5f -> {
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

@Preview
@Composable
private fun RatingScreenPreview() {
    RatingScreen()
}
