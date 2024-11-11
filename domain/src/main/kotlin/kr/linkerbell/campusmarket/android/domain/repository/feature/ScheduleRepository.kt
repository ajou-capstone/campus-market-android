package kr.linkerbell.campusmarket.android.domain.repository.feature

import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule

interface ScheduleRepository {
    suspend fun getSchedule(
        id: Long
    ): Result<List<Schedule>>

    suspend fun editSchedule(
        scheduleList: List<Schedule>
    ): Result<Unit>
}
