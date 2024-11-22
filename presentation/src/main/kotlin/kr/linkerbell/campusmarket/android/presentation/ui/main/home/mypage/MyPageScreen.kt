package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Black
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.changecampus.ChangeCampusConstant
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.LogoutConstant

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

    val userProfile = data.myProfile

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
            Text(
                text = "내 프로필",
                style = Headline2.merge(Gray900),
                modifier = Modifier.padding(start = 20.dp),
                color = Black
            )
            RippleBox(
                modifier = Modifier.padding(end = Space20),
                onClick = {
                    //navController.safeNavigate(NotificationConstant.ROUTE)
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(Space24),
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
//프로필 설정 페이지로 이동
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
                        text = "프로필 수정",
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
                            navController.navigate(ChangeCampusConstant.ROUTE)
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
                        text = "캠퍼스 변경",
                        style = Headline3,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "최근 활동",
                    style = Headline2,
                    color = Black,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            //최근 거래 목록 페이지로 이동
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
                        text = "최근 거래 목록",
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
                            //최근 작성된 리뷰 페이지로 이동
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
                        text = "최근 작성된 리뷰",
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
                            //찜한 목록으로 이동
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
                        text = "찜한 목록",
                        style = Headline3,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    text = "서비스/설정",
                    style = Headline2,
                    color = Black,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            //(알림)설정 페이지로 이동
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
                        text = "설정",
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
                            //문의하기 페이지로 이동
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
                        text = "문의",
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
                            //약관 페이지로 이동 (Notion)
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
                            navController.navigate(LogoutConstant.ROUTE)
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
                        text = "로그아웃/탈퇴",
                        style = Headline3,
                        color = Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
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
                    text = " (${userProfile.rating})",
                    style = Body1,
                    color = Black,
                )
            }
        }
    }
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
