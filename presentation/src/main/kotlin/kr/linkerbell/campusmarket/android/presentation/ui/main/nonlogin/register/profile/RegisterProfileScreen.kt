package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
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
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.common.gallery.GalleryScreen
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry.EntryConstant

@Composable
fun RegisterProfileScreen(
    navController: NavController,
    argument: RegisterProfileArgument
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var image: GalleryImage? by rememberSaveable { mutableStateOf(null) }
    var nickname: String by rememberSaveable { mutableStateOf("") }

    val isNicknameSizeValid = nickname.toByteArray(Charsets.UTF_8).size in 0..20
    val isNicknameFormValid = nickname.matches("[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣0-9]*".toRegex())
    var isNicknameServerCheckSuccess: Boolean by remember { mutableStateOf(true) }
    val isConfirmButtonEnabled =
        state != RegisterProfileState.Loading && nickname.isNotBlank() && isNicknameSizeValid && isNicknameFormValid && isNicknameServerCheckSuccess

    var isGalleryShowing by remember { mutableStateOf(false) }

    if (isGalleryShowing) {
        GalleryScreen(
            navController = navController,
            onDismissRequest = { isGalleryShowing = false },
            onResult = {
                image = it.getOrNull(0)
            }
        )
    }

    fun navigateToEntry() {
        navController.safeNavigate(EntryConstant.ROUTE) {
            popUpTo(RegisterProfileConstant.ROUTE) {
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
        Box {
            Card(
                modifier = Modifier.size(150.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(White),
                elevation = cardElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                border = BorderStroke(3.dp, Red400)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            isGalleryShowing = true
                        }
                ) {
                    image?.let {
                        AsyncImage(
                            model = it.filePath,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Space40),
                        painter = painterResource(R.drawable.img_user_default),
                        contentDescription = null
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = Red400,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        modifier = Modifier.size(34.dp),
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = null,
                        tint = White
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(Space20))
        Text(
            text = "프로필 설정",
            style = Headline0
        )
        Spacer(modifier = Modifier.height(Space12))
        Text(
            text = "사용하실 닉네임을 입력해주세요",
            style = Headline2
        )
        Spacer(modifier = Modifier.height(Space40))
        TypingTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Space40),
            text = nickname,
            hintText = "닉네임",
            isError = !isNicknameServerCheckSuccess,
            onValueChange = {
                nickname = it
                isNicknameServerCheckSuccess = true
            },
        )
        if (!isNicknameServerCheckSuccess) {
            Spacer(modifier = Modifier.height(Space8))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space44),
                text = "이미 사용중인 닉네임입니다.",
                style = Body1.merge(Red400),
            )
        } else if (!isNicknameFormValid) {
            Spacer(modifier = Modifier.height(Space8))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space44),
                text = "닉네임은 한글, 영문, 숫자만 입력 가능합니다.",
                style = Body1.merge(Red400),
            )
        } else if (!isNicknameSizeValid) {
            Spacer(modifier = Modifier.height(Space8))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Space44),
                text = "닉네임은 한글 최대 10자, 영어 최대 20자까지 입력 가능합니다.",
                style = Body1.merge(Red400),
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
                intent(
                    RegisterProfileIntent.OnConfirm(
                        nickname = nickname,
                        image = image
                    )
                )
            }
        ) { style ->
            if (state == RegisterProfileState.Loading) {
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

    fun checkNickname(event: RegisterProfileEvent.CheckNickname) {
        when (event) {
            RegisterProfileEvent.CheckNickname.Failure -> {
                isNicknameServerCheckSuccess = false
            }
        }
    }

    fun setProfile(event: RegisterProfileEvent.SetProfile) {
        when (event) {
            RegisterProfileEvent.SetProfile.Success -> {
                navigateToEntry()
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is RegisterProfileEvent.CheckNickname -> {
                    checkNickname(event)
                }

                is RegisterProfileEvent.SetProfile -> {
                    setProfile(event)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegisterProfileScreenPreview() {
    RegisterProfileScreen(
        navController = rememberNavController(),
        argument = RegisterProfileArgument(
            state = RegisterProfileState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        )
    )
}
