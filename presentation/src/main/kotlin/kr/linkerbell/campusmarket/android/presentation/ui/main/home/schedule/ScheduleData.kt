package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule

import androidx.compose.runtime.Immutable
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile

@Immutable
data class ScheduleData(
    val myProfile: MyProfile,
    val scheduleList: List<Schedule>
)
