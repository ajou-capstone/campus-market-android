package kr.linkerbell.boardlink.android.presentation.ui.main.home.mypage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.boardlink.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.boardlink.android.domain.model.nonfeature.randomuserprofile.RandomUserProfile
import kr.linkerbell.boardlink.android.domain.model.nonfeature.user.Profile
import kr.linkerbell.boardlink.android.presentation.common.theme.Body0
import kr.linkerbell.boardlink.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.boardlink.android.presentation.common.util.compose.LaunchedEffectWithLifecycle

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
        val profile by viewModel.profile.collectAsStateWithLifecycle()
        val randomUserProfile by viewModel.randomUserProfile.collectAsStateWithLifecycle()

        MyPageData(
            profile = profile,
            randomUserProfile = randomUserProfile
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
private fun MyPageScreen(
    navController: NavController,
    argument: MyPageArgument,
    data: MyPageData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(top = 50.dp)) {
            Text(
                text = "MyPageScreen",
                style = Body0
            )
            Spacer(modifier = Modifier.padding(50.dp))
            Text(
                text = "${data.randomUserProfile.fullName}, (${data.randomUserProfile.gender})"
            )
        }

    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->

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
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = MyPageData(
            profile = Profile.empty,
            randomUserProfile = RandomUserProfile.empty
        )
    )
}
