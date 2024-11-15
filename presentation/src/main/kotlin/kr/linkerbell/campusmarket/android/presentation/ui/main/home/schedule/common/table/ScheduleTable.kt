package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.common.table

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import kotlinx.datetime.LocalTime
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.presentation.common.theme.Body2
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray400
import kr.linkerbell.campusmarket.android.presentation.common.theme.Gray900
import kr.linkerbell.campusmarket.android.presentation.common.theme.Green400
import kr.linkerbell.campusmarket.android.presentation.common.theme.White
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.measureTextHeight
import kr.linkerbell.campusmarket.android.presentation.common.util.compose.measureTextWidth

@Composable
fun ScheduleTable(
    modifier: Modifier = Modifier,
    dataList: List<ScheduleTableData>,
    startTime: LocalTime,
    endTime: LocalTime,
    onClick: (Schedule) -> Unit = {}
) {
    val textMeasurer = rememberTextMeasurer()

    val cellCount = endTime.hour - startTime.hour + 1

    val dayOfWeekList: List<String> = listOf("월", "화", "수", "목", "금", "토", "일")

    fun getTimeText(time: LocalTime): String {
        return runCatching {
            String.format(Locale.KOREA, "%02d:%02d", time.hour, time.minute)
        }.getOrDefault("00:00")
    }

    val dayOfWeekSizeList: List<Pair<Dp, Dp>> = dayOfWeekList.map { text ->
        val textWidth = measureTextWidth(text = text, style = Body2)
        val textHeight = measureTextHeight(text = text, style = Body2)
        Pair(textWidth, textHeight)
    }

    val timeSizeList: List<Pair<Dp, Dp>> = List(cellCount) { index ->
        val time = LocalTime(startTime.hour + index, 0)
        val text = getTimeText(time)
        val textWidth = measureTextWidth(text = text, style = Body2)
        val textHeight = measureTextHeight(text = text, style = Body2)
        Pair(textWidth, textHeight)
    }

    Canvas(
        modifier = modifier.pointerInput(dataList) {
            detectTapGestures(
                onTap = { offset ->
                    val top = (timeSizeList.firstOrNull()?.second?.div(2)?.toPx() ?: 0f) +
                            (dayOfWeekSizeList.maxOfOrNull { it.second }?.toPx()
                                ?: 0f) + 8.dp.toPx()
                    val bottom =
                        size.height - (timeSizeList.lastOrNull()?.second?.div(2)?.toPx() ?: 0f)
                    val start = (timeSizeList.maxOfOrNull { it.first }?.toPx() ?: 0f) + 8.dp.toPx()
                    val heightPerHour = (bottom - top) / (cellCount - 1)
                    val widthPerDayOfWeek = (size.width - start) / 7

                    val item = dataList.flatMap { data ->
                        data.scheduleList.map { schedule ->
                            val scheduleTop =
                                top + heightPerHour * max(
                                    0,
                                    (schedule.startTime.toSecondOfDay() - startTime.toSecondOfDay())
                                ) / 3600
                            val scheduleBottom = top + heightPerHour * min(
                                endTime.toSecondOfDay() - startTime.toSecondOfDay(),
                                schedule.endTime.toSecondOfDay() - startTime.toSecondOfDay()
                            ) / 3600
                            val scheduleStart = start + widthPerDayOfWeek * (schedule.dayOfWeek - 1)
                            val scheduleEnd = start + widthPerDayOfWeek * schedule.dayOfWeek

                            schedule to Rect(
                                Offset(scheduleStart, scheduleTop),
                                Offset(scheduleEnd, scheduleBottom)
                            )
                        }
                    }.find { (_, rect) ->
                        offset.x in rect.left..rect.right
                                && offset.y in rect.top..rect.bottom
                    }?.first

                    if (item != null) {
                        onClick(item)
                    }
                }
            )
        }
    ) {
        val top = (timeSizeList.firstOrNull()?.second?.div(2)?.toPx() ?: 0f) +
                (dayOfWeekSizeList.maxOfOrNull { it.second }?.toPx() ?: 0f) + 8.dp.toPx()
        val bottom = size.height - (timeSizeList.lastOrNull()?.second?.div(2)?.toPx() ?: 0f)
        val start = (timeSizeList.maxOfOrNull { it.first }?.toPx() ?: 0f) + 8.dp.toPx()
        val heightPerHour = (bottom - top) / (cellCount - 1)
        val widthPerDayOfWeek = (size.width - start) / 7

        drawLine(
            color = Gray400,
            start = Offset(
                x = start,
                y = top - 8.dp.toPx()
            ),
            end = Offset(
                x = start,
                y = size.height
            ),
            strokeWidth = 1f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        dayOfWeekList.forEachIndexed { index, text ->
            drawText(
                textMeasurer = textMeasurer,
                text = text,
                topLeft = Offset(
                    x = start + (size.width - start) * (index + 0.5f) / dayOfWeekList.size -
                            (dayOfWeekSizeList.getOrNull(index)?.first?.toPx()?.div(2) ?: 0f),
                    y = 0f
                ),
                style = Body2.merge(Gray900)
            )
        }

        repeat(cellCount) { index ->
            val text = getTimeText(LocalTime(startTime.hour + index, 0))
            val cellY = top + heightPerHour * index
            drawText(
                textMeasurer = textMeasurer,
                text = text,
                topLeft = Offset(
                    x = 0f,
                    y = cellY - (timeSizeList.getOrNull(index)?.second?.toPx()?.div(2) ?: 1f)
                ),
                style = Body2.merge(Gray900)
            )
            drawLine(
                color = Gray400,
                start = Offset(
                    x = start,
                    y = cellY
                ),
                end = Offset(
                    x = size.width,
                    y = cellY
                ),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )
        }

        dataList.forEach { data ->
            data.scheduleList.forEach { schedule ->
                val scheduleTop =
                    top + heightPerHour * max(
                        0,
                        (schedule.startTime.toSecondOfDay() - startTime.toSecondOfDay())
                    ) / 3600
                val scheduleBottom = top + heightPerHour * min(
                    endTime.toSecondOfDay() - startTime.toSecondOfDay(),
                    schedule.endTime.toSecondOfDay() - startTime.toSecondOfDay()
                ) / 3600
                val scheduleStart = start + widthPerDayOfWeek * (schedule.dayOfWeek - 1)
                val scheduleEnd = start + widthPerDayOfWeek * schedule.dayOfWeek

                drawRect(
                    color = data.color.copy(alpha = 0.3f),
                    topLeft = Offset(
                        x = scheduleStart,
                        y = scheduleTop
                    ),
                    size = Size(
                        width = scheduleEnd - scheduleStart,
                        height = scheduleBottom - scheduleTop
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun ScheduleTablePreview1() {
    ScheduleTable(
        modifier = Modifier
            .size(width = 1080.dp, height = 1920.dp)
            .background(White),
        dataList = emptyList(),
        startTime = LocalTime(9, 0),
        endTime = LocalTime(22, 0)
    )
}

@Preview
@Composable
private fun ScheduleTablePreview2() {
    val scheduleList: MutableList<Schedule> = remember {
        mutableStateListOf(
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
    }

    ScheduleTable(
        modifier = Modifier
            .size(width = 1080.dp, height = 1920.dp)
            .background(White),
        dataList = listOf(
            ScheduleTableData(
                color = Gray400,
                scheduleList = scheduleList
            )
        ),
        startTime = LocalTime(9, 0),
        endTime = LocalTime(22, 0),
        onClick = { schedule ->
            scheduleList.remove(schedule)
        }
    )
}

@Preview
@Composable
private fun ScheduleTablePreview3() {
    ScheduleTable(
        modifier = Modifier
            .size(width = 1080.dp, height = 1920.dp)
            .background(White),
        dataList = listOf(
            ScheduleTableData(
                color = Green400,
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
            ),
            ScheduleTableData(
                color = Green400,
                scheduleList = listOf(
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
        ),
        startTime = LocalTime(9, 0),
        endTime = LocalTime(22, 0)
    )
}
