package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.compare

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.plus
import kotlinx.datetime.LocalTime
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.eventObserve
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.presentation.R
import kr.linkerbell.campusmarket.android.presentation.common.theme.Blue400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Headline2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Red400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space20
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space24
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space52
import kr.linkerbell.campusmarket.android.presentation.common.theme.Space56
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.LaunchedEffectWithLifecycle
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.safeNavigateUp
import kr.linkerbell.campusmarket.android.presentation.common.view.RippleBox
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.common.table.ScheduleTable
import kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.common.table.ScheduleTableData

@Composable
fun ScheduleCompareScreen(
    navController: NavController,
    argument: ScheduleCompareArgument,
    data: ScheduleCompareData
) {
    val (state, event, intent, logEvent, coroutineContext) = argument
    val scope = rememberCoroutineScope() + coroutineContext

    val startTime = LocalTime(9, 0)
    val endTime = LocalTime(22, 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Row(
            modifier = Modifier
                .height(Space56)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RippleBox(
                modifier = Modifier.padding(start = Space20),
                onClick = {
                    navController.safeNavigateUp()
                }
            ) {
                Icon(
                    modifier = Modifier.size(Space24),
                    painter = painterResource(R.drawable.ic_chevron_left),
                    contentDescription = null,
                    tint = Gray900
                )
            }
            Text(
                text = "스케줄 비교",
                style = Headline2.merge(Gray900),
                modifier = Modifier
                    .padding(start = Space20)
                    .weight(1f)
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ScheduleTable(
                modifier = Modifier
                    .height(Space52 * (endTime.hour - startTime.hour + 1))
                    .fillMaxWidth()
                    .padding(Space20),
                dataList = listOf(
                    ScheduleTableData(
                        color = Blue400,
                        scheduleList = data.mySchedule
                    ),
                    ScheduleTableData(
                        color = Red400,
                        scheduleList = data.userSchedule
                    )
                ),
                startTime = startTime,
                endTime = endTime
            )
            Spacer(modifier = Modifier.height(Space20))
        }
    }

    LaunchedEffectWithLifecycle(event, coroutineContext) {
        event.eventObserve { event ->

        }
    }
}

@Preview
@Composable
private fun ScheduleCompareScreenPreview() {
    ScheduleCompareScreen(
        navController = rememberNavController(),
        argument = ScheduleCompareArgument(
            state = ScheduleCompareState.Init,
            event = MutableEventFlow(),
            intent = {},
            logEvent = { _, _ -> },
            coroutineContext = CoroutineExceptionHandler { _, _ -> }
        ),
        data = ScheduleCompareData(
            mySchedule = listOf(
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
            ),
            userSchedule = listOf(
                Schedule(
                    dayOfWeek = 1,
                    startTime = LocalTime(9, 0),
                    endTime = LocalTime(15, 0)
                ),
                Schedule(
                    dayOfWeek = 2,
                    startTime = LocalTime(12, 0),
                    endTime = LocalTime(18, 0)
                ),
                Schedule(
                    dayOfWeek = 3,
                    startTime = LocalTime(15, 0),
                    endTime = LocalTime(21, 0)
                ),
                Schedule(
                    dayOfWeek = 4,
                    startTime = LocalTime(18, 0),
                    endTime = LocalTime(22, 0)
                ),
                Schedule(
                    dayOfWeek = 5,
                    startTime = LocalTime(21, 0),
                    endTime = LocalTime(22, 0)
                )
            )
        )
    )
}
