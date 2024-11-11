package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.edit

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule

@Immutable
data class ScheduleEditData(
    val initialSchedule: Schedule?
)
