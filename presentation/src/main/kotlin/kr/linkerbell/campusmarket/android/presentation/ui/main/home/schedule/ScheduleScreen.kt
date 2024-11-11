package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space32
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space52
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.ErrorObserver
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.common.table.ScheduleTable
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.common.table.ScheduleTableData
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.edit.ScheduleEditScreen

@Composable
fun ScheduleScreen(
    navController: NavController,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val argument: ScheduleArgument = Unit.let {
        val state by viewModel.state.collectAsStateWithLifecycle()

        ScheduleArgument(
            state = state,
            event = viewModel.event,
            intent = viewModel::onIntent,
            logEvent = viewModel::logEvent,
            coroutineContext = viewModel.coroutineContext
        )
    }

    val data: ScheduleData = Unit.let {
        val profile by viewModel.myProfile.collectAsStateWithLifecycle()
        val scheduleList by viewModel.scheduleList.collectAsStateWithLifecycle()

        ScheduleData(
            myProfile = profile,
            scheduleList = scheduleList
        )
    }

    ErrorObserver(viewModel)
    ScheduleScreen(
        navController = navController,
        argument = argument,
        data = data
    )
}

@Composable
private fun ScheduleScreen(
    navController: NavController,
    argument: ScheduleArgument,
    data: ScheduleData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val startTime = LocalTime(9, 0)
    val endTime = LocalTime(22, 0)

    var isScheduleAddShowing: Boolean by remember { mutableStateOf(false) }
    var editingSchedule: Schedule? by remember { mutableStateOf(null) }

    fun navigateToNotificationScreen() {
//        navController.safeNavigate(NotificationConstant.ROUTE)
    }

    if (isScheduleAddShowing) {
        ScheduleEditScreen(
            navController = navController,
            initialSchedule = null,
            onDismissRequest = { isScheduleAddShowing = false },
            onCancel = { },
            onConfirm = {
                intent(ScheduleIntent.AddSchedule(it))
            }
        )
    }

    if (editingSchedule != null) {
        ScheduleEditScreen(
            navController = navController,
            initialSchedule = editingSchedule,
            onDismissRequest = { editingSchedule = null },
            onCancel = {
                val editingSchedule = editingSchedule ?: return@ScheduleEditScreen
                intent(ScheduleIntent.RemoveSchedule(editingSchedule))
            },
            onConfirm = {
                val editingSchedule = editingSchedule ?: return@ScheduleEditScreen
                intent(
                    ScheduleIntent.UpdateSchedule(
                        from = editingSchedule,
                        to = it
                    )
                )
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
            Text(
                text = "시간표",
                style = Headline2.merge(Gray900),
                modifier = Modifier
                    .padding(start = Space20)
                    .weight(1f)
            )
            RippleBox(
                modifier = Modifier.padding(end = Space20),
                onClick = {
                    navigateToNotificationScreen()
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
            ScheduleTable(
                modifier = Modifier
                    .height(Space52 * (endTime.hour - startTime.hour + 1))
                    .fillMaxWidth()
                    .padding(Space20),
                dataList = listOf(
                    ScheduleTableData(
                        color = Blue400,
                        scheduleList = data.scheduleList
                    )
                ),
                startTime = startTime,
                endTime = endTime,
                onClick = { schedule ->
                    editingSchedule = schedule
                }
            )
            Spacer(modifier = Modifier.height(Space20))
        }
        Box(
            modifier = Modifier
                .padding(Space20)
                .constrainAs(button) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            FloatingActionButton(
                modifier = Modifier.size(Space56),
                shape = CircleShape,
                containerColor = Blue400,
                onClick = {
                    isScheduleAddShowing = true
                }
            ) {
                Icon(
                    modifier = Modifier.size(Space32),
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                    tint = White
                )
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
private fun ScheduleScreenPreview() {
    ScheduleScreen(
        navController = rememberNavController(),
        argument = ScheduleArgument(
            state = ScheduleState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = ScheduleData(
            myProfile = MyProfile.empty,
            scheduleList = listOf(
                Schedule(
                    dayOfWeek = 1,
                    startTime = LocalTime(9, 0),
                    endTime = LocalTime(12, 0)
                ),
                Schedule(
                    dayOfWeek = 1,
                    startTime = LocalTime(15, 0),
                    endTime = LocalTime(16, 30)
                ),
                Schedule(
                    dayOfWeek = 2,
                    startTime = LocalTime(10, 30),
                    endTime = LocalTime(12, 0)
                ),
                Schedule(
                    dayOfWeek = 2,
                    startTime = LocalTime(15, 0),
                    endTime = LocalTime(16, 30)
                ),
                Schedule(
                    dayOfWeek = 3,
                    startTime = LocalTime(12, 0),
                    endTime = LocalTime(15, 0)
                ),
                Schedule(
                    dayOfWeek = 4,
                    startTime = LocalTime(10, 30),
                    endTime = LocalTime(13, 30)
                ),
                Schedule(
                    dayOfWeek = 5,
                    startTime = LocalTime(10, 30),
                    endTime = LocalTime(12, 0)
                )
            )
        )
    )
}
