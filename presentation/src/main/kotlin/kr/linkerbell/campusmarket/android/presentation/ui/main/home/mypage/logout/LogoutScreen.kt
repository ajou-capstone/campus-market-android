package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout

import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.withdrawal.WithdrawalConstant
import kotlin.system.exitProcess

@Composable
fun LogoutScreen(
    navController: NavController,
    argument: LogoutArgument
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var isLogoutDialogVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun restartApp() {
        context.packageManager.getLaunchIntentForPackage(context.packageName)?.let { intent ->
            context.startActivity(
                Intent.makeRestartActivityTask(intent.component)
            )
        }
        exitProcess(0)
    }
    if (isLogoutDialogVisible) {
        LogoutDialog(
            onConfirm = {
                argument.intent(LogoutIntent.LogOut)
                restartApp()
            },
            onDismissRequest = { isLogoutDialogVisible = false }
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
                    text = "로그아웃 및 탈퇴",
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
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        isLogoutDialogVisible = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_error),
                    contentDescription = null,
                    tint = Blue400
                )
                Text(
                    text = "로그아웃",
                    style = Headline3,
                    color = Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(WithdrawalConstant.ROUTE)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_error),
                    contentDescription = null,
                    tint = Blue400
                )
                Text(
                    text = "탈퇴하기",
                    style = Headline3,
                    color = Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->

        }
    }
}

@Composable
private fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    DialogScreen(
        title = "로그아웃 하시겠습니까?",
        isCancelable = true,
        onConfirm = { onConfirm() },
        onCancel = {},
        onDismissRequest = {
            onDismissRequest()
        }
    )
}

@Preview
@Composable
private fun LogoutScreenPreview() {
    LogoutScreen(
        navController = rememberNavController(),
        argument = LogoutArgument(
            state = LogoutState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        )
    )
}
