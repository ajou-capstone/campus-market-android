package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.keyword

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
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
import kr.linkerbell.campusmarket.android.domain.model.feature.mypage.Keyword
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField

@Composable
fun KeywordScreen(
    navController: NavController,
    argument: KeywordArgument,
    data: KeywordData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val keywordList = data.myKeywords

    var newKeyword by remember { mutableStateOf("") }
    var isKeywordDuplicatedDialogVisible by remember { mutableStateOf(false) }

    if (isKeywordDuplicatedDialogVisible) {
        KeywordDuplicatedDialog(
            onDismissRequest = { isKeywordDuplicatedDialogVisible = false }
        )
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
                text = "키워드 설정",
                style = Headline2
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
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
                    text = "키워드 등록",
                    style = Headline2,
                    color = Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TypingTextField(
                    text = newKeyword,
                    onValueChange = { newKeyword = it },
                    maxLines = 1,
                    hintText = "추가할 키워드를 입력하세요",
                    maxTextLength = 100,
                    trailingIconContent = {
                        if (newKeyword.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add button",
                                tint = Gray900,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        argument.intent(KeywordIntent.PostKeyword(newKeyword))
                                        newKeyword = ""
                                    }
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            argument.intent(KeywordIntent.PostKeyword(newKeyword))
                            newKeyword = ""
                        }
                    ),
                )
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Column {
                Text(
                    text = "나의 키워드 목록",
                    style = Headline2,
                    color = Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column {
                    keywordList.forEach { keyword ->
                        KeywordCard(
                            keyword = keyword,
                            onDeleteIconClicked = {
                                argument.intent(
                                    KeywordIntent.DeleteKeyword(keyword.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    LaunchedEffectWithLifecycle(event) {
        event.eventObserve { event ->
            when (event) {
                KeywordEvent.NewKeywordPosted.Success -> {
                    argument.intent(KeywordIntent.RefreshKeyword)
                }

                KeywordEvent.KeywordDeleted.Success -> {
                    argument.intent(KeywordIntent.RefreshKeyword)
                }

                KeywordEvent.NewKeywordPosted.Duplicated -> {
                    isKeywordDuplicatedDialogVisible = true
                }
            }
        }
    }
}

@Composable
private fun KeywordDuplicatedDialog(
    onDismissRequest: () -> Unit
) {
    DialogScreen(
        title = "이미 등록된 키워드입니다",
        onConfirm = { onDismissRequest() },
        onDismissRequest = {
            onDismissRequest()
        }
    )
}

@Composable
private fun KeywordCard(
    keyword: Keyword,
    onDeleteIconClicked: (Long) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = keyword.keyword,
            style = Body1,
            color = Gray900,
            modifier = Modifier.padding(start = 4.dp)
        )
        RippleBox(
            modifier = Modifier
                .padding(4.dp),
            onClick = {
                onDeleteIconClicked(keyword.id)
            }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Gray900
            )
        }
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Gray900,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Preview
@Composable
private fun KeywordScreenPreview() {
    KeywordScreen(
        navController = rememberNavController(),
        argument = KeywordArgument(
            state = KeywordState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = KeywordData(
            myKeywords = listOf(
                Keyword(id = 1, keyword = "키워드1"),
                Keyword(id = 2, keyword = "키워드33")
            )
        )
    )
}
