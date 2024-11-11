package kr.linkerbell.campusmarket.android.data.remote.network.model.feature.schedule

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.linkerbell.campusmarket.android.data.remote.mapper.DataMapper
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule

@Serializable
data class GetScheduleRes(
    @SerialName("timetable")
    val timetable: List<GetScheduleItemRes>
) : DataMapper<List<Schedule>> {
    override fun toDomain(): List<Schedule> {
        return timetable.map { it.toDomain() }
    }
}

@Serializable
data class GetScheduleItemRes(
    @SerialName("dayOfWeek")
    val dayOfWeek: Int,
    @SerialName("startTime")
    val startTime: LocalTime,
    @SerialName("endTime")
    val endTime: LocalTime,
) : DataMapper<Schedule> {
    override fun toDomain(): Schedule {
        return Schedule(
            dayOfWeek = dayOfWeek,
            startTime = startTime,
            endTime = endTime
        )
    }
}
