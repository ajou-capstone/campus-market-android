package kr.linkerbell.boardlink.android.presentation.ui.main.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kr.linkerbell.boardlink.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.boardlink.android.presentation.R
import kr.linkerbell.boardlink.android.presentation.common.util.compose.LaunchedEffectWithLifecycle

// 각 항목의 아이콘은 임시로 지정함
@Composable
fun SettingScreen(
    navController: NavController,
    argument: SettingArgument
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .systemBarsPadding(),
    ) {
        // 임시로 생성한 TopBar
        TemporaryTopBar()

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {

            Text(text = "설정", style = MaterialTheme.typography.headlineMedium)

            FullSizeSettingButton("프로필 설정",
                menuIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_box_unchecked),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.padding(8.dp)
                    )
                },
                onClick = { /* Navigate to proper screen */ }
            )

            FullSizeSettingButton("문의하기",
                menuIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_box_unchecked),
                        contentDescription = "Inquiry Icon",
                        modifier = Modifier.padding(8.dp)
                    )
                },
                onClick = { /* Navigate to proper screen */ }
            )

            FullSizeSettingButton("약관 확인",
                menuIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_box_unchecked),
                        contentDescription = "Agreement Icon",
                        modifier = Modifier.padding(8.dp)
                    )
                },
                onClick = { /* Navigate to proper screen */ }
            )

            FullSizeSettingButton("알림 설정",
                menuIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_box_unchecked),
                        contentDescription = "Notification Icon",
                        modifier = Modifier.padding(8.dp)
                    )
                },
                onClick = { /* Navigate to proper screen */ }
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                FixedSizeSettingButton("로그아웃",
                    menuIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_box_unchecked),
                            contentDescription = "Logout Icon",
                            modifier = Modifier.padding(8.dp)
                        )
                    },
                    onClick = { /* Navigate to proper screen */ }
                )

                FixedSizeSettingButton("탈퇴",
                    menuIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_box_unchecked),
                            contentDescription = "Withdrawal Icon",
                            modifier = Modifier.padding(8.dp)
                        )
                    },
                    onClick = { /* Navigate to proper screen */ }
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
fun FullSizeSettingButton(
    menuName: String,
    menuIcon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(0.dp) // 기본 패딩 제거 -> 혹시 기본 설정된 패딩이 있는지?
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            menuIcon()
            Text(
                text = menuName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}


@Composable
fun FixedSizeSettingButton(
    menuName: String,
    menuIcon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
        contentPadding = PaddingValues(0.dp), // 기본 패딩 제거
        modifier = Modifier.size(140.dp, 38.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            menuIcon()
            Text(
                text = menuName,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun TemporaryTopBar(modifier: Modifier = Modifier){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) //Android standard
            //.background(MaterialTheme.colorScheme.background)
            .background(Color(0xFF00BCD4)) // Temp
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ){
        Text(
            text = "BoardLink Logo",
            modifier = Modifier
                .border(1.dp, Color(0xFF000000))
                .padding(2.dp)
        )
    }

}

@Preview
@Composable
fun SettingMenusPreview() {

    FullSizeSettingButton("Preview Menu Name",
        menuIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_box_unchecked),
                contentDescription = "Preview Menu Icon",
                modifier = Modifier.padding(8.dp)
            )
        },
        onClick = { /* Navigate to proper screen */ }
    )
}

@Preview
@Composable
private fun SettingScreenPreview() {
    SettingScreen(
        navController = rememberNavController(),
        argument = SettingArgument(
            state = SettingState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        )
    )
}

@Preview
@Composable
private fun TemporaryTopBarPreview(){
    TemporaryTopBar()
}
