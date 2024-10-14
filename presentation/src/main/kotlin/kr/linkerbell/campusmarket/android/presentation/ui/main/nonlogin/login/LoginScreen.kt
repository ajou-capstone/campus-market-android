package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline3
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space12
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space32
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space80
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.HomeConstant
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    argument: LoginArgument
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext
    val context = LocalContext.current

    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )
    val googleOAuthClientId = stringResource(id = R.string.id_google_oauth_client)
    val isConfirmButtonEnabled = argument.state != LoginState.Loading

    fun navigateToHome() {
        navController.navigate(HomeConstant.ROUTE) {
            popUpTo(LoginConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    fun requestLogin() {
        val googleIdOption: GetSignInWithGoogleOption =
            GetSignInWithGoogleOption.Builder(googleOAuthClientId)
                .build()

        val googleLoginRequest: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        scope.launch {
            runCatching {
                val credentialManager = CredentialManager.create(context)
                val result = credentialManager.getCredential(
                    request = googleLoginRequest,
                    context = context,
                )

                if (
                    result.credential is CustomCredential
                    && result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(result.credential.data)
                    intent(
                        LoginIntent.Login(
                            idToken = googleIdTokenCredential.idToken
                        )
                    )
                } else {
                    // TODO: 에러 처리
                    Timber.d("asdfasdf error : ${result.credential}")
                }
            }.onFailure {
                Timber.d(it)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Space80))
        Image(
            modifier = Modifier.size(Space80),
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(Space32))
        Text(
            text = "Campus Market",
            style = Headline0
        )
        Spacer(modifier = Modifier.height(Space12))
        Text(
            text = "대학생활의 모든 것을 한 곳에서",
            style = Headline3
        )
        Spacer(modifier = Modifier.weight(1f))
        ConfirmButton(
            modifier = Modifier
                .padding(start = Space20, end = Space20, bottom = Space12)
                .fillMaxWidth(),
            properties = ConfirmButtonProperties(
                size = ConfirmButtonSize.Large,
                type = ConfirmButtonType.Primary
            ),
            isEnabled = isConfirmButtonEnabled,
            onClick = {
                requestLogin()
            }
        ) { style ->
            Text(
                text = "구글로 로그인",
                style = style
            )
        }
    }

    fun login(event: LoginEvent.Login) {
        when (event) {
            LoginEvent.Login.Success -> {
                navigateToHome()
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is LoginEvent.Login -> login(event)
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        navController = rememberNavController(),
        argument = LoginArgument(
            state = LoginState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        )
    )
}
