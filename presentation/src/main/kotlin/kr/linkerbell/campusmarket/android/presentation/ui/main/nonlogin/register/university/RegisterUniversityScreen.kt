package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.university

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.common.REGEX_EMAIL
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space12
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space40
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space44
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space8
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry.EntryConstant

@Composable
fun RegisterUniversityScreen(
    navController: NavController,
    argument: RegisterUniversityArgument
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var email: String by rememberSaveable { mutableStateOf("") }
    var verifyCode: String by rememberSaveable { mutableStateOf("") }

    val isEmailFormValid = email.matches(REGEX_EMAIL.toRegex())
    var isEmailServerCheckSuccess: Boolean by rememberSaveable { mutableStateOf(true) }
    var isEmailSend: Boolean by rememberSaveable { mutableStateOf(false) }
    var isEmailSendButtonLoading: Boolean by rememberSaveable { mutableStateOf(false) }
    val isEmailSendButtonEnabled =
        !isEmailSendButtonLoading && email.isNotBlank() && isEmailFormValid && isEmailServerCheckSuccess

    var verifyCodeTimer: Long by rememberSaveable { mutableLongStateOf(0) }
    var isVerifyCodeServerCheckSuccess: Boolean by rememberSaveable { mutableStateOf(true) }
    var isConfirmButtonLoading: Boolean by rememberSaveable { mutableStateOf(false) }
    val isConfirmButtonEnabled =
        !isConfirmButtonLoading && verifyCode.isNotBlank() && isVerifyCodeServerCheckSuccess && verifyCodeTimer > 0

    fun navigateToEntry() {
        navController.safeNavigate(EntryConstant.ROUTE) {
            popUpTo(RegisterUniversityConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "대학교 이메일 인증",
            style = Headline0.merge(Gray900)
        )
        Spacer(modifier = Modifier.height(Space12))
        Text(
            text = "이메일 인증이 필요해요",
            style = Headline2.merge(Gray900)
        )
        Spacer(modifier = Modifier.height(Space40))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(Space24))
            TypingTextField(
                modifier = Modifier.weight(1f),
                text = email,
                hintText = "이메일",
                isError = !isEmailServerCheckSuccess,
                isEnabled = !isEmailSend,
                onValueChange = {
                    email = it
                    isEmailServerCheckSuccess = true
                }
            )
            Spacer(modifier = Modifier.width(Space12))
            ConfirmButton(
                properties = ConfirmButtonProperties(
                    size = ConfirmButtonSize.Medium,
                    type = ConfirmButtonType.Primary
                ),
                isEnabled = isEmailSendButtonEnabled,
                onClick = {
                    isEmailSendButtonLoading = true
                    intent(
                        RegisterUniversityIntent.OnSendEmail(
                            email = email
                        )
                    )
                }
            ) { style ->
                if (isEmailSendButtonLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(Space24),
                        color = White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "전송",
                        style = style
                    )
                }
            }
            Spacer(modifier = Modifier.width(Space24))
        }
        if (!isEmailServerCheckSuccess) {
            Spacer(modifier = Modifier.height(Space8))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space44),
                text = "적절하지 않은 이메일입니다.",
                style = Body1.merge(Red400),
            )
        } else if (!isEmailFormValid && email.isNotBlank()) {
            Spacer(modifier = Modifier.height(Space8))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space44),
                text = "이메일 형식을 맞춰주세요.",
                style = Body1.merge(Red400),
            )
        }
        Spacer(modifier = Modifier.height(Space24))
        TypingTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Space24),
            text = verifyCode,
            hintText = "인증번호",
            isError = !isVerifyCodeServerCheckSuccess,
            isEnabled = isEmailSend,
            onValueChange = {
                verifyCode = it
                isVerifyCodeServerCheckSuccess = true
            }
        )
        if (isEmailSend) {
            Spacer(modifier = Modifier.height(Space8))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space44),
                text = if (verifyCodeTimer > 0) "$verifyCodeTimer 초 남았습니다." else "인증번호가 만료되었습니다.\n다시 전송해주세요.",
                style = Body1.merge(Gray900),
            )
        }
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
                isConfirmButtonLoading = true
                intent(
                    RegisterUniversityIntent.OnConfirm(
                        verifyCode = verifyCode
                    )
                )
            }
        ) { style ->
            if (isConfirmButtonLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(Space24),
                    color = White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "다음",
                    style = style
                )
            }
        }
    }

    fun checkEmail(event: RegisterUniversityEvent.CheckEmail) {
        when (event) {
            RegisterUniversityEvent.CheckEmail.Failure -> {
                isEmailServerCheckSuccess = false
                isEmailSendButtonLoading = false
            }

            RegisterUniversityEvent.CheckEmail.Success -> {
                isEmailSendButtonLoading = false
                isEmailSend = true
                verifyCodeTimer = 300L
            }
        }
    }

    fun checkVerifyCode(event: RegisterUniversityEvent.CheckVerifyCode) {
        when (event) {
            RegisterUniversityEvent.CheckVerifyCode.Failure -> {
                isVerifyCodeServerCheckSuccess = false
                isConfirmButtonLoading = false
            }

            RegisterUniversityEvent.CheckVerifyCode.Success -> {
                isConfirmButtonLoading = false
                navigateToEntry()
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is RegisterUniversityEvent.CheckEmail -> {
                    checkEmail(event)
                }

                is RegisterUniversityEvent.CheckVerifyCode -> {
                    checkVerifyCode(event)
                }
            }
        }
    }

    LaunchedEffect(verifyCodeTimer) {
        if (verifyCodeTimer > 0) {
            delay(1000)
            verifyCodeTimer -= 1
        }
    }
}

@Preview
@Composable
private fun RegisterUniversityScreenPreview() {
    RegisterUniversityScreen(
        navController = rememberNavController(),
        argument = RegisterUniversityArgument(
            state = RegisterUniversityState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        )
    )
}
