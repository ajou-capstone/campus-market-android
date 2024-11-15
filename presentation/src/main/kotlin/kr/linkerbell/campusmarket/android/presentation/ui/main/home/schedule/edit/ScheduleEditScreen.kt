package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kotlinx.datetime.LocalTime
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray200
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline0
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space40
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButton
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonProperties
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonSize
import kr.linkerbell.campusmarket.android.presentation.common.view.confirm.ConfirmButtonType
import kr.linkerbell.campusmarket.android.presentation.common.view.dropdown.TextDropdownMenu

@Composable
fun ScheduleEditScreen(
    navController: NavController,
    initialSchedule: Schedule?,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: (Schedule) -> Unit,
    viewModel: ScheduleEditViewModel = hiltViewModel()
) {
    val argument: ScheduleEditArgument = Unit.let {
        val state by viewModel.state.collectAsStateWithLifecycle()

        ScheduleEditArgument(
            state = state,
            event = viewModel.event,
            intent = viewModel::onIntent,
            logEvent = viewModel::logEvent,
            coroutineContext = viewModel.coroutineContext
        )
    }

    val data: ScheduleEditData = Unit.let {
        ScheduleEditData(
            initialSchedule = initialSchedule
        )
    }

    ErrorObserver(viewModel)
    ScheduleEditScreen(
        navController = navController,
        argument = argument,
        data = data,
        onDismissRequest = onDismissRequest,
        onCancel = onCancel,
        onConfirm = onConfirm
    )
}

@Composable
private fun ScheduleEditScreen(
    navController: NavController,
    argument: ScheduleEditArgument,
    data: ScheduleEditData,
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: (Schedule) -> Unit
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val initialSchedule = data.initialSchedule ?: Schedule(
        dayOfWeek = 1,
        startTime = LocalTime(9, 0),
        endTime = LocalTime(9, 0)
    )
    var scheduleDayOfWeek: Int by remember { mutableIntStateOf(initialSchedule.dayOfWeek) }
    var scheduleStartTimeHour: String by remember { mutableStateOf(initialSchedule.startTime.hour.toString()) }
    var scheduleStartTimeMinute: String by remember { mutableStateOf(initialSchedule.startTime.minute.toString()) }
    var scheduleEndTimeHour: String by remember { mutableStateOf(initialSchedule.endTime.hour.toString()) }
    var scheduleEndTimeMinute: String by remember { mutableStateOf(initialSchedule.endTime.minute.toString()) }

    var isDayOfWeekExpanded by remember { mutableStateOf(false) }

    val startTimeHour: Int = scheduleStartTimeHour.toIntOrNull()?.takeIf { it in 9..22 } ?: -1
    val startTimeMinute: Int = scheduleStartTimeMinute.toIntOrNull()?.takeIf { it in 0..59 } ?: -1
    val endTimeHour: Int = scheduleEndTimeHour.toIntOrNull()?.takeIf { it in 9..22 } ?: -1
    val endTimeMinute: Int = scheduleEndTimeMinute.toIntOrNull()?.takeIf { it in 0..59 } ?: -1

    val isConfirmEnabled: Boolean = startTimeHour != -1
            && startTimeMinute != -1
            && endTimeHour != -1
            && endTimeMinute != -1
            && LocalTime(startTimeHour, startTimeMinute) < LocalTime(endTimeHour, endTimeMinute)

    fun getDayOfWeekText(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "월요일"
            2 -> "화요일"
            3 -> "수요일"
            4 -> "목요일"
            5 -> "금요일"
            6 -> "토요일"
            7 -> "일요일"
            else -> ""
        }
    }

    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = White),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = 25.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (data.initialSchedule == null) "스케줄 추가" else "스케줄 수정",
                    style = Headline0
                )
                Spacer(modifier = Modifier.height(Space20))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RippleBox(
                        onClick = {
                            isDayOfWeekExpanded = !isDayOfWeekExpanded
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = getDayOfWeekText(scheduleDayOfWeek),
                                style = Body0.merge(Gray900)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chevron_down),
                                contentDescription = null,
                                modifier = Modifier.size(Space24),
                                tint = Gray900
                            )
                            TextDropdownMenu(
                                items = (1..7).toList(),
                                label = { dayOfWeek ->
                                    getDayOfWeekText(dayOfWeek)
                                },
                                isExpanded = isDayOfWeekExpanded,
                                onDismissRequest = { isDayOfWeekExpanded = false }
                            ) { dayOfWeek ->
                                scheduleDayOfWeek = dayOfWeek
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        BasicTextField(
                            value = scheduleStartTimeHour,
                            modifier = Modifier
                                .width(Space40)
                                .background(
                                    color = Gray200,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            onValueChange = {
                                scheduleStartTimeHour = it.filter { it.isDigit() }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = Body0.merge(Gray900),
                            singleLine = true
                        )
                        Text(
                            text = " : ",
                            style = Body0.merge(Gray900)
                        )
                        BasicTextField(
                            value = scheduleStartTimeMinute,
                            modifier = Modifier
                                .width(Space40)
                                .background(
                                    color = Gray200,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            onValueChange = {
                                scheduleStartTimeMinute = it.filter { it.isDigit() }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = Body0.merge(Gray900),
                            singleLine = true
                        )
                        Text(
                            text = " ~ ",
                            style = Body0.merge(Gray900)
                        )
                        BasicTextField(
                            value = scheduleEndTimeHour,
                            modifier = Modifier
                                .width(Space40)
                                .background(
                                    color = Gray200,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            onValueChange = {
                                scheduleEndTimeHour = it.filter { it.isDigit() }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = Body0.merge(Gray900),
                            singleLine = true
                        )
                        Text(
                            text = " : ",
                            style = Body0.merge(Gray900)
                        )
                        BasicTextField(
                            value = scheduleEndTimeMinute,
                            modifier = Modifier
                                .width(Space40)
                                .background(
                                    color = Gray200,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            onValueChange = {
                                scheduleEndTimeMinute = it.filter { it.isDigit() }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = Body0.merge(Gray900),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.wrapContentSize()) {
                    ConfirmButton(
                        properties = ConfirmButtonProperties(
                            size = ConfirmButtonSize.Large,
                            type = ConfirmButtonType.Secondary
                        ),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onCancel()
                            onDismissRequest()
                        }
                    ) { style ->
                        Text(
                            text = if (data.initialSchedule == null) "취소" else "삭제",
                            style = style
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    ConfirmButton(
                        properties = ConfirmButtonProperties(
                            size = ConfirmButtonSize.Large,
                            type = ConfirmButtonType.Primary
                        ),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onConfirm(
                                Schedule(
                                    dayOfWeek = scheduleDayOfWeek,
                                    startTime = LocalTime(
                                        startTimeHour,
                                        startTimeMinute
                                    ),
                                    endTime = LocalTime(
                                        endTimeHour,
                                        endTimeMinute
                                    )
                                )
                            )
                            onDismissRequest()
                        },
                        isEnabled = isConfirmEnabled
                    ) { style ->
                        Text(
                            text = if (data.initialSchedule == null) "추가" else "수정",
                            style = style
                        )
                    }
                }
            }
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->

        }
    }
}

@Preview
@Composable
private fun ScheduleEditScreenPreview() {
    ScheduleEditScreen(
        navController = rememberNavController(),
        argument = ScheduleEditArgument(
            state = ScheduleEditState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = ScheduleEditData(
            initialSchedule = Schedule(
                dayOfWeek = 1,
                startTime = LocalTime(9, 0),
                endTime = LocalTime(10, 30)
            )
        ),
        onDismissRequest = {},
        onCancel = {},
        onConfirm = {}
    )
}
