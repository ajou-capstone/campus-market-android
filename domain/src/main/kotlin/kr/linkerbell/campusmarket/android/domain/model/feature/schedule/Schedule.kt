package kr.linkerbell.campusmarket.android.domain.model.feature.schedule

import kotlinx.datetime.LocalTime

data class Schedule(
    val dayOfWeek: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
)
