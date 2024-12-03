package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.edit.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
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
import kr.linkerbell.campusmarket.android.presentation.common.theme.Caption1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.DialogScreen
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.image.PostImage
import kr.linkerbell.campusmarket.android.presentation.common.view.textfield.TypingTextField
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage
import kr.linkerbell.campusmarket.android.presentation.ui.main.common.gallery.GalleryScreen

@Composable
fun ChangeProfileScreen(
    navController: NavController,
    argument: ChangeProfileArgument,
    data: ChangeProfileData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val focusRequester = remember { FocusRequester() }

    val originalProfileImage = data.myProfile.profileImage
    val originalNickname = data.myProfile.nickname
    var newProfileImage: GalleryImage? by rememberSaveable { mutableStateOf(null) }
    var newNickname: String? by remember { mutableStateOf(null) }
    var nicknameLength by remember { mutableIntStateOf(originalNickname.length) }

    var isSuccessDialogVisible by remember { mutableStateOf(false) }
    var isGalleryShowing by remember { mutableStateOf(false) }

    fun nicknameValidation(): Boolean {
        val changedNickname = newNickname
        if (changedNickname != null) {
            val isNicknameSizeValid = changedNickname.toByteArray(Charsets.UTF_8).size in 0..20
            val isNicknameFormValid = changedNickname.matches("[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣0-9]*".toRegex())

            return isNicknameSizeValid
                    && isNicknameFormValid
                    && (changedNickname != originalNickname)
        }
        return false
    }

    if (isSuccessDialogVisible) {
        DialogScreen(
            title = "등록 성공!",
            message = "프로필이 변경되었습니다.",
            isCancelable = false,
            onDismissRequest = {
                isSuccessDialogVisible = false
                navController.safeNavigateUp()
            }
        )
    }

    if (isGalleryShowing) {
        GalleryScreen(
            navController = navController,
            onDismissRequest = { isGalleryShowing = false },
            onResult = {
                newProfileImage = it.getOrNull(0)
            }
        )
    }
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
                    text = "프로필 수정",
                    style = Headline2.merge(Gray900),
                    color = Black
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
                        data = newProfileImage ?: originalProfileImage,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                isGalleryShowing = true
                            }
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
                TypingTextField(
                    text = newNickname ?: originalNickname,
                    onValueChange = {
                        nicknameLength = newNickname?.length ?: originalNickname.length
                        if (nicknameLength <= 10) {
                            newNickname = it
                        }
                    },
                    maxLines = 100,
                    hintText = "닉네임은 특수문자, 공백 없이 10자까지 가능해요",
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = Space20)
                )
                Text(
                    text = if (nicknameLength <= 10) "(${nicknameLength}/10)"
                    else "닉네임은 특수문자, 공백 없이 10자까지 가능해요",
                    modifier = Modifier.align(Alignment.End),
                    style = Caption1,
                    color = if (nicknameLength <= 10) Gray900 else Red400
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(Space20)
                .fillMaxWidth()
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                }
        ) {
            val nickname: String = newNickname ?: ""

            PostNewProfile(
                nickname.isNotEmpty() && newNickname != originalNickname,
                onClicked = {
                    if (nicknameValidation()) {
                        argument.intent(
                            ChangeProfileIntent.OnChangeNickname(nickname)
                        )
                    }
                    if (newProfileImage != null) {
                        argument.intent(
                            ChangeProfileIntent.OnChangeProfileImage(newProfileImage)
                        )
                    }
                }
            )
        }
    }
    LaunchedEffectWithLifecycle(event, coroutineContext) {
        argument.intent(ChangeProfileIntent.RefreshProfile)
        event.eventObserve { event ->
            when (event) {
                is ChangeProfileEvent.SetProfile.Success -> {
                    isSuccessDialogVisible = true
                }
            }
        }
    }
}

@Composable
private fun PostNewProfile(
    isPostAvailable: Boolean,
    onClicked: () -> Unit
) {
    val backgroundColor = if (isPostAvailable) Blue400 else Gray200
    val buttonText = if (isPostAvailable) "수정" else "수정"

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .fillMaxWidth()
            .clickable {
                onClicked()
            },
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = buttonText,
            modifier = Modifier.padding(8.dp),
            style = Headline2,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun ChangeProfileScreenPreview() {
    ChangeProfileScreen(
        navController = rememberNavController(),
        argument = ChangeProfileArgument(
            state = ChangeProfileState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = Dispatchers.IO
        ),
        data = ChangeProfileData(
            myProfile = MyProfile(
                id = -1,
                campusId = -1,
                loginEmail = "",
                schoolEmail = "",
                nickname = "User_Nickname",
                profileImage = "",
                rating = 10.0
            )
        )
    )
}
