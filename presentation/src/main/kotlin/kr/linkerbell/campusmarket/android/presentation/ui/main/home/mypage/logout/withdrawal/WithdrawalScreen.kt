package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.withdrawal

import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kotlin.system.exitProcess

@Composable
fun WithdrawalScreen(
    navController: NavController,
    argument: WithdrawalArgument,
    data: WithdrawalData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var isWithdrawalRequested by remember { mutableStateOf(false) }
    var isWithdrawalSuccessDialogVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun restartApp() {
        context.packageManager.getLaunchIntentForPackage(context.packageName)?.let { intent ->
            context.startActivity(
                Intent.makeRestartActivityTask(intent.component)
            )
        }
        exitProcess(0)
    }

    if (isWithdrawalSuccessDialogVisible) {
        WithdrawalSuccessDialog(
            onDismissRequest = {
                restartApp()
            }
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
                    text = "탈퇴하기",
                    style = Headline2.merge(Gray900),
                    color = Black
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
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
                text = "${data.myProfile.nickname} 님,",
                style = Headline2,
                color = Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "Campus Market에서 정말 탈퇴하시겠습니까?",
                style = Headline2,
                color = Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "내가 설정한 모든 정보가 사라져요",
                style = Body1,
                color = Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "사라진 계정은 복구할 수 없어요",
                style = Body1,
                color = Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "내가 작성한 글은 여전히 남아있어요",
                style = Body1,
                color = Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Blue400)
                    .clickable {
                        if (!isWithdrawalRequested) {
                            isWithdrawalRequested = true
                            argument.intent(WithdrawalIntent.Withdrawal)
                        }
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "탈퇴할게요",
                    modifier = Modifier.padding(8.dp),
                    style = Headline2,
                    color = Color.White
                )
            }

            // TODO : 탈퇴 절차와 관련된 논의 후 마저 작성
        }

        LaunchedEffectWithLifecycle(event, coroutineContext) {
            event.eventObserve { event ->
                when (event) {
                    is WithdrawalEvent.WithdrawalSuccess -> {
                        isWithdrawalSuccessDialogVisible = true
                    }
                }
            }
        }
    }
}

@Composable
private fun WithdrawalSuccessDialog(
    onDismissRequest: () -> Unit
) {
    DialogScreen(
        title = "탈퇴 완료",
        message = "다음에 다시 만나요!",
        isCancelable = false,
        onDismissRequest = { onDismissRequest() }
    )
}

@Preview
@Composable
private fun WithdrawalScreenPreview() {
    WithdrawalScreen(
        navController = rememberNavController(),
        argument = WithdrawalArgument(
            state = WithdrawalState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = WithdrawalData(MyProfile.empty.copy(nickname = "siri22"))
    )
}
