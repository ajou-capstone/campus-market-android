package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.term

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.term.Term
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue700
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space12
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space32
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space80
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry.EntryConstant
import timber.log.Timber

@Composable
fun RegisterTermScreen(
    navController: NavController,
    argument: RegisterTermArgument,
    data: RegisterTermData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val checkedTermList = remember { mutableStateListOf<Long>() }

    val isNecessaryTermChecked = data.termList.all { term ->
        !term.isRequired || checkedTermList.contains(term.id)
    }
    val isConfirmButtonEnabled = state != RegisterTermState.Loading && isNecessaryTermChecked

    fun navigateToEntry() {
        navController.safeNavigate(EntryConstant.ROUTE) {
            popUpTo(RegisterTermConstant.ROUTE) {
                inclusive = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = "서비스 약관동의",
            style = Headline0.merge(Gray900)
        )
        Spacer(modifier = Modifier.height(Space12))
        Text(
            text = "서비스 이용 동의가 필요해요",
            style = Headline2.merge(Gray900)
        )
        Spacer(modifier = Modifier.height(Space80))
        data.termList.forEach { term ->
            RegisterTermScreenItem(
                term = term,
                isChecked = checkedTermList.contains(term.id),
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        checkedTermList.add(term.id)
                    } else {
                        checkedTermList.remove(term.id)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(Space80))
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
                intent(RegisterTermIntent.OnConfirm(checkedTermList))
            }
        ) { style ->
            Text(
                text = "다음",
                style = style
            )
        }
    }

    fun patchTerm(event: RegisterTermEvent.AgreeTerm) {
        when (event) {
            RegisterTermEvent.AgreeTerm.Success -> {
                navigateToEntry()
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is RegisterTermEvent.AgreeTerm -> {
                    patchTerm(event)
                }
            }
        }
    }
}

@Composable
fun RegisterTermScreenItem(
    term: Term,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val context = LocalContext.current

    val text = if (term.isRequired) {
        "[필수] ${term.title}"
    } else {
        "[선택] ${term.title}"
    }

    fun navigateToTermLink() {
        runCatching {
            val link = Uri.parse(term.url)
            val browserIntent = Intent(Intent.ACTION_VIEW, link)
            ContextCompat.startActivity(context, browserIntent, null)
        }.onFailure { exception ->
            Timber.d(exception)
            Firebase.crashlytics.recordException(exception)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space32, vertical = Space12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (term.url.isEmpty()) {
            Text(
                text = text,
                style = Body1.merge(
                    color = Gray900
                )
            )
        } else {
            Text(
                modifier = Modifier.clickable {
                    navigateToTermLink()
                },
                text = text,
                style = Body1.merge(
                    color = Gray900,
                    textDecoration = TextDecoration.Underline
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        RippleBox(
            onClick = {
                onCheckedChange(!isChecked)
            }
        ) {
            Icon(
                modifier = Modifier.size(Space24),
                painter = painterResource(id = R.drawable.ic_check_line),
                contentDescription = null,
                tint = if (isChecked) Blue700 else Gray400
            )
        }
    }
}

@Preview
@Composable
private fun RegisterTermScreenPreview() {
    RegisterTermScreen(
        navController = rememberNavController(),
        argument = RegisterTermArgument(
            state = RegisterTermState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = RegisterTermData(
            termList = listOf(
                Term(
                    id = 0L,
                    title = "개인정보 수집 이용동의",
                    url = "https://www.naver.com",
                    isRequired = true,
                    isAgree = true
                ),
                Term(
                    id = 1L,
                    title = "고유식별 정보처리 동의",
                    url = "https://www.naver.com",
                    isRequired = true,
                    isAgree = false
                ),
                Term(
                    id = 2L,
                    title = "통신사 이용약관 동의",
                    url = "https://www.naver.com",
                    isRequired = true,
                    isAgree = false
                ),
                Term(
                    id = 3L,
                    title = "서비스 이용약관 동의",
                    url = "https://www.naver.com",
                    isRequired = false,
                    isAgree = false
                )
            )
        )
    )
}
