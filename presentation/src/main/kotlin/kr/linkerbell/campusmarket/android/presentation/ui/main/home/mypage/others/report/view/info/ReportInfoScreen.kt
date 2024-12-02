package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.view.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kotlinx.datetime.LocalDateTime
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.ReportInfo
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox

@Composable
fun ReportInfoScreen(
    navController: NavController,
    argument: ReportInfoArgument,
    data: ReportInfoData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val inquiryData = data.reportInfo

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RippleBox(
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
                    text = "문의 상세 정보",
                    style = Headline2.merge(Gray900),
                    color = Black
                )
            }
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .constrainAs(contents) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            Column {
                Text(
                    text = inquiryData.title,
                    style = Headline2,
                    color = Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "문의 정보 : ${translateToKor(inquiryData.category)}",
                    style = Caption2,
                    color = Gray600,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "문의 일시 : ${inquiryData.createdDate}",
                    style = Caption2,
                    color = Gray600,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = inquiryData.description,
                    style = Body2,
                    color = Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Gray900,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                )
            }
            if (inquiryData.isCompleted) {
                Text(
                    text = "답변 내용",
                    style = Headline2,
                    color = Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = inquiryData.answerDescription,
                    style = Body2,
                    color = Black,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "답변 일시 : ${inquiryData.answerDate}",
                    style = Caption2,
                    color = Gray600,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                Text(
                    text = "문의 사항을 확인하고 있습니다",
                    style = Body2,
                    color = Gray600,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

private fun translateToKor(engCategory: String): String {
    return when (engCategory) {
        "ACCOUNT_INQUIRY" -> "계정 문의"
        "CHAT_AND_NOTIFICATION" -> "채팅, 알림"
        "SECONDHAND_TRANSACTION" -> "중고거래"
        "ADVERTISEMENT_INQUIRY" -> "광고 문의"
        "OTHER" -> "기타"
        else -> "기타"
    }
}

@Preview
@Composable
private fun InquiryInfoScreenPreview() {
    ReportInfoScreen(
        navController = rememberNavController(),
        argument = ReportInfoArgument(
            state = ReportInfoState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = ReportInfoData(
            ReportInfo(
                qaId = 12345L,
                userId = 67890L,
                title = "Sample Inquiry Title",
                description = "This is a sample inquiry description providing details about the issue.",
                answerDescription = "This is a sample answer to the inquiry.",
                category = "General",
                isCompleted = true,
                createdDate = LocalDateTime(2024, 11, 22, 14, 30, 0),
                answerDate = LocalDateTime(2024, 12, 22, 14, 30, 0),
            )
        )
    )
}
