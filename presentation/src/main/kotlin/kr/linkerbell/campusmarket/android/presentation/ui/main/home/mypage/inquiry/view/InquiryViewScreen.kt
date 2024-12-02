package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
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
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.UserInquiry
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray600
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.isEmpty
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.inquiry.info.InquiryInfoConstant

@Composable
fun InquiryViewScreen(
    navController: NavController,
    argument: InquiryViewArgument,
    data: InquiryViewData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val inquiryList by remember { mutableStateOf(data.inquiryList) }

    fun onClicked(qaId: Long) {
        val inquiryInfoRoute = makeRoute(
            route = InquiryInfoConstant.ROUTE,
            arguments = mapOf(
                InquiryInfoConstant.ROUTE_ARGUMENT_QA_ID to qaId.toString()
            )
        )
        navController.safeNavigate(inquiryInfoRoute)
    }

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
                text = "문의 내역 보기",
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
            Text(
                text = "전체 문의 내역이 표시됩니다.",
                style = Headline2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = Black,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            )
            if (inquiryList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "보여줄 항목이 없어요",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                }
            }

            LazyColumn {
                items(
                    count = inquiryList.itemCount,
                    key = { index -> inquiryList[index]?.qaId ?: -1 }
                ) { index ->
                    val inquiry = inquiryList[index] ?: return@items
                    InquiryCard(
                        inquiry = inquiry,
                        onClicked = { qaId -> onClicked(qaId) }
                    )
                }
            }

        }
    }
}

@Composable
private fun InquiryCard(
    inquiry: UserInquiry,
    onClicked: (Long) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable {
                onClicked(inquiry.qaId)
            }
    ) {
        if (inquiry.isCompleted) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
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
                        text = "답변 완료",
                        style = Caption2,
                        color = Gray600,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = "답변 대기",
                    style = Caption2,
                    color = Gray600,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        Column {
            Text(
                text = inquiry.title,
                style = Headline2,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = Black,
                modifier = Modifier.padding(bottom = 4.dp, end = 100.dp)
            )
            Text(
                text = "문의 종류 : ${translateToKor(inquiry.category)}",
                style = Caption2,
                color = Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "문의 일자 : ${inquiry.createdDate}",
                style = Caption2,
                color = Gray600,
            )
            if (inquiry.isCompleted) {
                Text(
                    text = "답변 일자 : ${inquiry.answerDate}",
                    style = Caption2,
                    color = Gray600,
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray600,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
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
private fun InquiryCardPreview() {
    InquiryCard(
        UserInquiry(
            qaId = 1L,
            userId = 12345L,
            category = "General",
            title = "Sample Question Title is too looooooooooooooooooooooooong",
            isCompleted = true,
            createdDate = LocalDateTime(2024, 11, 22, 14, 30, 0),
            answerDate = LocalDateTime(2024, 11, 22, 15, 30, 0)
        ),
        onClicked = {}
    )
}

@Preview
@Composable
private fun InquiryViewScreenPreview() {
    InquiryViewScreen(
        navController = rememberNavController(),
        argument = InquiryViewArgument(
            state = InquiryViewState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = InquiryViewData(
            MutableStateFlow(
                PagingData.from(
                    listOf(
                        UserInquiry(
                            qaId = 1L,
                            userId = 12345L,
                            category = "General",
                            title = "Sample Question Title",
                            isCompleted = true,
                            createdDate = LocalDateTime(2024, 11, 22, 14, 30, 0),
                            answerDate = LocalDateTime(2024, 11, 22, 15, 30, 0)
                        ),
                        UserInquiry(
                            qaId = 10L,
                            userId = 12345L,
                            category = "General",
                            title = "Sample Question Title",
                            isCompleted = false,
                            createdDate = LocalDateTime(2024, 11, 22, 14, 30, 0),
                            answerDate = LocalDateTime(2024, 11, 22, 15, 30, 0)
                        )
                    )
                )
            ).collectAsLazyPagingItems()
        )
    )
}