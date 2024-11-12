package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.compare

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule

@Immutable
data class ScheduleCompareData(
    val mySchedule: List<Schedule>,
    val userSchedule: List<Schedule>
)
