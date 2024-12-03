package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.inquiry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.report.InquiryCategoryList
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField

@Composable
fun InquiryScreen(
    navController: NavController,
    argument: InquiryArgument,
    data: InquiryData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var userInquiryTitle by remember { mutableStateOf("") }
    var titleLength by remember { mutableIntStateOf(0) }

    var userInquiryContent by remember { mutableStateOf("") }
    var descriptionLength by remember { mutableIntStateOf(0) }

    var isContentAvailable by remember { mutableStateOf(true) }
    var isPostAvailable by remember { mutableStateOf(true) }

    var isValidationDialogVisible by remember { mutableStateOf(false) }
    var validationDialogContent by remember { mutableStateOf("") }
    var isSuccessDialogVisible by remember { mutableStateOf(false) }

    var currentSelectedCategory by remember { mutableStateOf("OTHER") }

    fun validateContent() {
        isContentAvailable = true
        if (titleLength == 0) {
            isContentAvailable = false
            validationDialogContent = "제목을 입력해주세요."
        }
        if (descriptionLength == 0) {
            isContentAvailable = false
            validationDialogContent = "문의 세부 내용을 입력해주세요."
        }
    }

    if (isValidationDialogVisible) {
        isPostAvailable = true
        ContentValidationWarningDialog(
            validationDialogContent = validationDialogContent,
            onDismissRequest = { isValidationDialogVisible = false }
        )
    }

    if (isSuccessDialogVisible) {
        SuccessToPostDialog(
            onDismissRequest = {
                isSuccessDialogVisible = false
                navController.popBackStack()
            }
        )
    }

    ConstraintLayout(
        modifier = Modifier
            .background(White)
            .fillMaxSize()
    ) {
        val (topBar, contents, button) = createRefs()
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
                            .size(40.dp)
                            .padding(horizontal = 8.dp)
                    )
                }
                Text(
                    text = "문의하기",
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
                    text = "문의 정보 (제목)",
                    style = Headline2.merge(Gray900),
                    color = Black,
                    modifier= Modifier.padding(bottom = 8.dp)
                )
                TypingTextField(
                    text = userInquiryTitle,
                    onValueChange = {
                        titleLength = userInquiryTitle.length
                        if (titleLength <= 50) {
                            userInquiryTitle = it
                        }
                    },
                    maxLines = 100,
                    hintText = "제목은 최대 50자까지 작성할 수 있어요",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = if (titleLength <= 50) "(${titleLength}/50)"
                    else "제목은 최대 50자까지 작성할 수 있어요!",
                    modifier = Modifier.align(Alignment.End),
                    style = Caption1,
                    color = if (descriptionLength <= 200) Gray900 else Red400
                )
            }
            Column {
                Text(
                    text = "문의 종류",
                    style = Headline2.merge(Gray900),
                    color = Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                CategorySelectBox(
                    inquiryCategoryList = data.inquiryCategoryList,
                    currentSelectedCategory = currentSelectedCategory,
                    onClick = { selectedCategory ->
                        currentSelectedCategory = selectedCategory
                    }
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text(
                    text = "문의 세부 정보",
                    style = Headline2.merge(Gray900),
                    color = Black
                )
                TypingTextField(
                    text = userInquiryContent,
                    onValueChange = {
                        descriptionLength = userInquiryContent.length
                        if (descriptionLength <= 500) {
                            userInquiryContent = it
                        }
                    },
                    maxLines = 100,
                    hintText = "문의 내용은 최대 500자까지 작성할 수 있어요", //TODO(문의 사항 최대 길이)
                    modifier = Modifier
                        .heightIn(min = 140.dp)
                        .padding(vertical = 8.dp)
                )
                Text(
                    text = if (descriptionLength <= 200) "(${descriptionLength}/500)"
                    else "문의 내용은 최대 500자까지 작성할 수 있어요!",
                    modifier = Modifier.align(Alignment.End),
                    style = Caption1,
                    color = if (descriptionLength <= 200) Gray900 else Red400
                )
            }
            Spacer(modifier = Modifier.padding(32.dp))
        }
        Box(
            modifier = Modifier
                .padding(Space20)
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                }
        ) {
            PostButton(
                isPostAvailable = isPostAvailable,
                onPostButtonClicked = {
                    validateContent()
                    if (isContentAvailable && isPostAvailable) {
                        isPostAvailable = false
                        argument.intent(
                            InquiryIntent.PostInquiry(
                                title = userInquiryTitle,
                                category = currentSelectedCategory,
                                description = userInquiryContent
                            )
                        )
                    }
                    if (!isContentAvailable && isPostAvailable) {
                        isValidationDialogVisible = true
                    }
                }
            )
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is InquiryEvent.PostInquiry.Success -> {
                    isSuccessDialogVisible = true
                }

                is InquiryEvent.PostInquiry.Failure -> {
                    isPostAvailable = true
                }
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
        else -> "문의 종류를 선택해주세요"
    }
}

@Composable
private fun CategorySelectBox(
    inquiryCategoryList: List<String>,
    currentSelectedCategory: String,
    onClick: (String) -> Unit
) {
    val localConfiguration = LocalConfiguration.current

    var isDropDownExpanded by remember { mutableStateOf(false) }
    val itemIndex = remember {
        mutableIntStateOf(
            inquiryCategoryList.indexOf(currentSelectedCategory).takeIf { it >= 0 } ?: 4)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Gray200)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clickable {
                    isDropDownExpanded = !isDropDownExpanded
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = translateToKor(inquiryCategoryList[itemIndex.intValue]),
                maxLines = 1,
                style = Body1,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 8.dp, end = 4.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Search Button",
                tint = Gray900,
                modifier = Modifier
                    .size(24.dp)
            )
        }
        DropdownMenu(
            expanded = isDropDownExpanded,
            onDismissRequest = { isDropDownExpanded = false },
            modifier = Modifier
                .heightIn(max = 200.dp)
                .width(localConfiguration.screenWidthDp.dp * 3 / 5)
        ) {
            inquiryCategoryList.forEachIndexed { index, category ->
                DropdownMenuItem(
                    text = { Text(text = translateToKor(category)) },
                    onClick = {
                        isDropDownExpanded = false
                        itemIndex.intValue = index
                        onClick(category)
                    }
                )
            }
        }
    }
}

@Composable
private fun PostButton(
    isPostAvailable: Boolean,
    onPostButtonClicked: () -> Unit
) {
    val backgroundColor = if (isPostAvailable) Blue400 else Gray200
    val buttonText = if (isPostAvailable) "등록하기" else "등록중입니다"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable {
                if (isPostAvailable) {
                    onPostButtonClicked()
                }
            },
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = buttonText,
            modifier = Modifier.padding(8.dp),
            style = Headline2,
            color = Color.White
        )
    }
}

@Composable
private fun ContentValidationWarningDialog(
    validationDialogContent: String,
    onDismissRequest: () -> Unit
) {
    DialogScreen(
        title = "필수 항목 누락",
        message = validationDialogContent,
        isCancelable = false,
        onDismissRequest = { onDismissRequest() }
    )
}

@Composable
private fun SuccessToPostDialog(
    onDismissRequest: () -> Unit
) {
    DialogScreen(
        title = "등록 완료",
        message = "문의 내용이 접수되었습니다.",
        isCancelable = false,
        onConfirm = {},
        onDismissRequest = { onDismissRequest() }
    )
}

@Preview
@Composable
private fun InquiryPostScreenPreview() {
    InquiryScreen(
        navController = rememberNavController(),
        argument = InquiryArgument(
            state = InquiryState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = InquiryData(
            inquiryCategoryList = InquiryCategoryList.empty.categoryList
        )
    )
}
