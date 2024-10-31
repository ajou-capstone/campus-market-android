package kr.linkerbell.campusmarket.android.presentation.ui.main.home.trade.tradepost

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.common.theme.Indigo50
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField

@Composable
fun TradePostScreen(
    navController: NavController,
    argument: TradePostArgument,
    data: TradePostData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    // 1. API 호출
    //  1.1. 무언가가 받아져오면 수정
    //  1.2. 아무것도 안받아져왔으면 새로 작성

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TradePostScreenTopBar(navigateUp = { navController.popBackStack() })
        TradePostScreenPostImage()
        TradePostScreenTradeInfo()
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->

        }
    }
}

@Composable
fun TradePostScreenTopBar(navigateUp: () -> Unit) {
    Row(
        modifier = Modifier
            .background(Indigo50)
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Navigate Up Button",
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navigateUp()
                }
                .padding(horizontal = 8.dp)
        )
        Text(
            text = "상품 등록"
        )
    }
}

@Composable
fun TradePostScreenPostImage() {
    TODO("Not yet implemented")
}

@Composable
fun TradePostScreenTradeInfo() {

    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("0") }
    var info by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(8.dp)
    ) {

        Column {
            Text(
                text = "제목",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TypingTextField(
                text = title,
                onValueChange = { title = it },
                hintText = "제목을 입력하세요",
                maxLines = 1,
                maxTextLength = 100,
                trailingIconContent = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear button",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                title = ""
                            }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Column {
            Text(
                text = "가격",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TypingTextField(
                text = price,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() })
                        price = newValue
                },
                hintText = "가격을 입력하세요",
                maxLines = 1,
                maxTextLength = 100,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                trailingIconContent = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear button",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                price = "0"
                            }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        Column {
            Text(
                text = "상품 상세 정보",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TypingTextField(
                text = info,
                onValueChange = { info = it },
                hintText = "상품 정보를 입력해주세요 (최대 1,000자)",
                maxLines = 1,
                maxTextLength = 1000,
                trailingIconContent = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear button",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                info = ""
                            }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TradePostScreenTradePostScreenTopBarPreview() {
    TradePostScreenTopBar({ })
}

@Preview
@Composable
private fun TradePostScreenTradeInfoPreview() {
    TradePostScreenTradeInfo()
}

@Preview
@Composable
private fun TradePostScreenPreview() {
    TradePostScreen(
        navController = rememberNavController(),
        argument = TradePostArgument(
            state = TradePostState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = TradePostData("")
    )
}

