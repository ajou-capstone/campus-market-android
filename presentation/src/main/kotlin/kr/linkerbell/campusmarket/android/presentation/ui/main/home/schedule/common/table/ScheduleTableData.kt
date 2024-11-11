package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.common.table

import androidx.compose.ui.graphics.Color
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule

data class ScheduleTableData(
    val color: Color,
    val scheduleList: List<Schedule>
)
