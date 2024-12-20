package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlin.system.exitProcess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.makeRoute
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.edit.campus.ChangeCampusConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.edit.profile.ChangeProfileConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.account.withdrawal.WithdrawalConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.notification.NotificationConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.others.report.view.list.ReportListConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.keyword.KeywordConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.likes.MyLikesConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_review.MyRecentReviewConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.recent.recent_trade.MyRecentTradeConstant
import timber.log.Timber

@Composable
fun MyPageScreen(
    navController: NavController,
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val argument: MyPageArgument = Unit.let {
        val state by viewModel.state.collectAsStateWithLifecycle()

        MyPageArgument(
            state = state,
            event = viewModel.event,
            intent = viewModel::onIntent,
            logEvent = viewModel::logEvent,
            coroutineContext = viewModel.coroutineContext
        )
    }

    val data: MyPageData = Unit.let {
        val profile by viewModel.myProfile.collectAsStateWithLifecycle()

        MyPageData(
            myProfile = profile
        )
    }

    ErrorObserver(viewModel)
    MyPageScreen(
        navController = navController,
        argument = argument,
        data = data
    )
}

@Composable
fun MyPageScreen(
    navController: NavController,
    argument: MyPageArgument,
    data: MyPageData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var isLogoutDialogVisible by remember { mutableStateOf(false) }
    val userProfile = data.myProfile

    val context = LocalContext.current

    fun restartApp() {
        context.packageManager.getLaunchIntentForPackage(context.packageName)?.let { intent ->
            context.startActivity(
                Intent.makeRestartActivityTask(intent.component)
            )
        }
        exitProcess(0)
    }

    fun navigateToTermLink() {
        runCatching {
            val link =
                Uri.parse("https://www.notion.so/Campus-Market-test-29c07360035e4f6589e17e06324f7b23")
            val browserIntent = Intent(Intent.ACTION_VIEW, link)
            ContextCompat.startActivity(context, browserIntent, null)
        }.onFailure { exception ->
            Timber.d(exception)
            Firebase.crashlytics.recordException(exception)
        }
    }

    if (isLogoutDialogVisible) {
        LogoutConfirmDialog(
            onLogoutConfirmButtonClicked = {
                argument.intent(MyPageIntent.LogOut)
            },
            onDismissRequest = {
                isLogoutDialogVisible = false
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
            Text(
                text = "내 프로필",
                style = Headline2.merge(Gray900),
                modifier = Modifier.padding(start = 20.dp),
                color = Black
            )
            RippleBox(
                modifier = Modifier.padding(end = Space20),
                onClick = {
                    navController.safeNavigate(NotificationConstant.ROUTE)
                }
            ) {
                Icon(
                    modifier = Modifier.size(Space24),
                    painter = painterResource(R.drawable.ic_notification),
                    contentDescription = null,
                    tint = Gray900
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .constrainAs(contents) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            MyProfileUserInfo(userProfile)
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray900,
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 16.dp)
            )

            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "프로필 설정",
                    style = Headline2,
                    color = Black,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.safeNavigate(ChangeProfileConstant.ROUTE)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_edit),
                        contentDescription = null,
                        tint = Blue400
                    )
                    Text(
                        text = "프로필 수정",
                        style = Body0,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.safeNavigate(ChangeCampusConstant.ROUTE)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_campus),
                        contentDescription = null,
                        tint = Blue400
                    )
                    Text(
                        text = "캠퍼스 변경",
                        style = Body0,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "나의 활동",
                    style = Headline2,
                    color = Black,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp, top = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            val newRoute = makeRoute(
                                route = MyRecentTradeConstant.ROUTE,
                                arguments = mapOf(
                                    MyRecentTradeConstant.ROUTE_ARGUMENT_USER_ID to userProfile.id.toString()
                                )
                            )
                            navController.safeNavigate(newRoute)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_box),
                        contentDescription = null,
                        tint = Blue400
                    )
                    Text(
                        text = "최근 거래 목록",
                        style = Body0,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            val newRoute = makeRoute(
                                route = MyRecentReviewConstant.ROUTE,
                                arguments = mapOf(
                                    MyRecentReviewConstant.ROUTE_ARGUMENT_USER_ID to userProfile.id.toString()
                                )
                            )
                            navController.safeNavigate(newRoute)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_list),
                        contentDescription = null,
                        tint = Blue400
                    )
                    Text(
                        text = "나의 리뷰",
                        style = Body0,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.safeNavigate(MyLikesConstant.ROUTE)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Blue400
                    )
                    Text(
                        text = "찜한 목록",
                        style = Body0,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.safeNavigate(KeywordConstant.ROUTE)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_keyword),
                        modifier = Modifier.size(16.dp),
                        contentDescription = null,
                        tint = Blue400
                    )
                    Text(
                        text = "키워드 설정",
                        style = Body0,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "서비스",
                    style = Headline2,
                    color = Black,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp, top = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.safeNavigate(ReportListConstant.ROUTE)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(R.drawable.ic_list),
                        contentDescription = null,
                        tint = Blue400
                    )
                    Text(
                        text = "문의/신고",
                        style = Body0,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        navigateToTermLink()
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
                    text = "약관 보기",
                    style = Body0,
                    color = Black,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }
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
                    painter = painterResource(R.drawable.ic_logout_2),
                    contentDescription = null,
                    tint = Blue400
                )
                Text(
                    text = "로그아웃",
                    style = Body0,
                    color = Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.safeNavigate(WithdrawalConstant.ROUTE)
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.ic_logout),
                    contentDescription = null,
                    tint = Blue400
                )
                Text(
                    text = "탈퇴하기",
                    style = Body0,
                    color = Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
    LaunchedEffectWithLifecycle(event, coroutineContext) {
        argument.intent(MyPageIntent.RefreshData)

        event.eventObserve { event ->
            when (event) {
                is MyPageEvent.LogOutSuccess -> {
                    restartApp()
                }
            }
        }
    }
}

@Composable
private fun MyProfileUserInfo(
    userProfile: MyProfile
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(150.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(White),
            elevation = cardElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
            border = BorderStroke(3.dp, Blue400)
        ) {
            PostImage(
                data = userProfile.profileImage,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = userProfile.nickname,
                style = Headline1,
                color = Black,
            )
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(R.drawable.filled_star),
                    contentDescription = null,
                    tint = Blue400
                )
                Text(
                    text = " (${String.format("%.1f", userProfile.rating)})",
                    style = Body1,
                    color = Black,
                )
            }
        }
    }
}

@Composable
private fun LogoutConfirmDialog(
    onLogoutConfirmButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    DialogScreen(
        title = "로그아웃 하시겠습니까?",
        isCancelable = true,
        onConfirm = { onLogoutConfirmButtonClicked() },
        onCancel = { onDismissRequest() },
        onDismissRequest = {
            onDismissRequest()
        }
    )
}

@Preview
@Composable
private fun MyPageScreenPreview() {
    MyPageScreen(
        navController = rememberNavController(),
        argument = MyPageArgument(
            state = MyPageState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = MyPageData(
            MyProfile(
                id = 12345L,
                campusId = 67890L,
                loginEmail = "user123@example.com",
                schoolEmail = "user123@university.edu",
                nickname = "CampusKing",
                profileImage = "https://picsum.photos/200",
                rating = 4.8
            )
        )
    )
}
