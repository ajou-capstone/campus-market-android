package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.campus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Campus
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue700
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline1
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space12
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space40
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space8
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigate
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry.EntryConstant

@Composable
fun RegisterCampusScreen(
    navController: NavController,
    argument: RegisterCampusArgument,
    data: RegisterCampusData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    var campusIndex: Int by remember { mutableIntStateOf(-1) }
    var isDropDownMenuExpanded: Boolean by remember { mutableStateOf(false) }
    var isConfirmButtonLoading: Boolean by rememberSaveable { mutableStateOf(false) }

    val isConfirmButtonEnabled = campusIndex != -1

    fun navigateToEntry() {
        navController.safeNavigate(EntryConstant.ROUTE) {
            popUpTo(RegisterCampusConstant.ROUTE) {
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
            text = "캠퍼스 선택",
            style = Headline0.merge(Gray900)
        )
        Spacer(modifier = Modifier.height(Space12))
        Text(
            text = "재학중인 대학교 캠퍼스를 입력하세요.",
            style = Headline2.merge(Gray900)
        )
        Spacer(modifier = Modifier.height(Space40))
        RippleBox(
            onClick = {
                isDropDownMenuExpanded = !isDropDownMenuExpanded
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = data.campusList.getOrNull(campusIndex)?.region ?: "캠퍼스 선택",
                    style = Headline1
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_down),
                    contentDescription = null,
                    modifier = Modifier.size(Space24),
                    tint = Gray900
                )
                DropdownMenu(
                    modifier = Modifier.background(White),
                    expanded = isDropDownMenuExpanded,
                    onDismissRequest = { isDropDownMenuExpanded = false },
                ) {
                    data.campusList.mapIndexed { index, campus ->
                        DropdownMenuItem(
                            text = {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = Space8),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = campus.region,
                                            style = Body1.merge(Gray900)
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        if (index == campusIndex) {
                                            Icon(
                                                painter = painterResource(R.drawable.ic_check_line),
                                                contentDescription = null,
                                                tint = Blue700
                                            )
                                        }
                                    }
                                }
                            },
                            onClick = {
                                campusIndex = index
                                isDropDownMenuExpanded = false
                            },
                            contentPadding = PaddingValues(0.dp)
                        )
                    }
                }
            }
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
                val id = data.campusList.getOrNull(campusIndex)?.id ?: return@ConfirmButton

                isConfirmButtonLoading = true
                intent(
                    RegisterCampusIntent.OnConfirm(
                        id = id
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

    fun setCampus(event: RegisterCampusEvent.SetCampus) {
        when (event) {
            RegisterCampusEvent.SetCampus.Success -> {
                isConfirmButtonLoading = false
                navigateToEntry()
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->
            when (event) {
                is RegisterCampusEvent.SetCampus -> {
                    setCampus(event)
                }
            }
        }
    }
}

@Preview
@Composable
private fun RegisterCampusScreenPreview() {
    RegisterCampusScreen(
        navController = rememberNavController(),
        argument = RegisterCampusArgument(
            state = RegisterCampusState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = RegisterCampusData(
            campusList = listOf(
                Campus(
                    id = 1L,
                    region = "원주 캠퍼스"
                ),
                Campus(
                    id = 2L,
                    region = "춘천 캠퍼스"
                )
            )
        )
    )
}
