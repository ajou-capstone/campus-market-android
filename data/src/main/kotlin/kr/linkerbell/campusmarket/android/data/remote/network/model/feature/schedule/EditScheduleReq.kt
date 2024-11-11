package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.schedule

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditScheduleReq(
    @SerialName("timetable")
    val timetable: List<EditScheduleItemReq>
)

@Serializable
data class EditScheduleItemReq(
    @SerialName("dayOfWeek")
    val dayOfWeek: Int,
    @SerialName("startTime")
    val startTime: LocalTime,
    @SerialName("endTime")
    val endTime: LocalTime,
)
