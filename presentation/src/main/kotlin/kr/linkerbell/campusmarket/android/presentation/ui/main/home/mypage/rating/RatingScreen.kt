package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.rating

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red400
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kotlin.math.roundToInt

@Composable
fun RatingScreen(
    navController: NavController,
    argument: RatingArgument,
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var userRating by remember { mutableIntStateOf(5) }
    var userDescription by remember { mutableStateOf("") }

    var descriptionLength by remember { mutableIntStateOf(0) }
    var isReviewRequested by remember { mutableStateOf(false) }
    var isRatingSuccessDialogVisible by remember { mutableStateOf(false) }

    if (isRatingSuccessDialogVisible) {
        DialogScreen(
            title = "작성된 리뷰가 등록되었습니다!",
            isCancelable = false,
            onConfirm = { },
            onDismissRequest = {
                isRatingSuccessDialogVisible = false
                navController.safeNavigateUp()
            }
        )
    }

    Dialog(
        onDismissRequest = {},
    ) {
        Column(
            modifier = Modifier
                .size(height = screenHeight * 0.6f, width = screenWidth * 0.8f)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
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

                TypingTextField(
                    text = userDescription,
                    onValueChange = {
                        descriptionLength = userDescription.length
                        if (descriptionLength <= 200) {
                            userDescription = it
                        }
                    },
                    maxLines = 100,
                    modifier = Modifier
                        .heightIn(min = 140.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Text(
                    text = if (descriptionLength <= 200) "(${descriptionLength}/200)"
                    else "리뷰는 최대 200자까지만 작성할 수 있어요!",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp),
                    style = Caption1,
                    color = if (descriptionLength <= 200) Gray900 else Red400
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.wrapContentSize()) {
                    ConfirmButton(
                        properties = ConfirmButtonProperties(
                            size = ConfirmButtonSize.Large,
                            type = ConfirmButtonType.Secondary
                        ),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.safeNavigateUp()
                        }
                    ) { style ->
                        Text(
                            text = "나중에 하기",
                            style = style
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    ConfirmButton(
                        properties = ConfirmButtonProperties(
                            size = ConfirmButtonSize.Large,
                            type = ConfirmButtonType.Primary
                        ),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            if (!isReviewRequested) {
                                isReviewRequested = true
                                argument.intent(RatingIntent.RateUser(userDescription, userRating))
                            }
                        }
                    ) { style ->
                        Text(
                            text = "평가하기",
                            style = style
                        )
                    }
                }
            }
        }
    }

    LaunchedEffectWithLifecycle(event) {
        event.eventObserve { event ->
            when (event) {
                is RatingEvent.RateSuccess -> {
                    isRatingSuccessDialogVisible = true
                }
            }

        }
    }
}

@Composable
private fun StarRatingBar(
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


@Preview
@Composable
private fun RatingScreenPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        RatingScreen(
            navController = rememberNavController(),
            argument = RatingArgument(
                state = RatingState.Init,
                event = MutableEventFlow(),
                intent = {},
                logEvent = { _, _ -> },
                coroutineContext = CoroutineExceptionHandler { _, _ -> }
            )
        )
    }
}
