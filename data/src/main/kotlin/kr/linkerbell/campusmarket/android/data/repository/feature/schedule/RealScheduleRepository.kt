package kr.linkerbell.campusmarket.android.data.repository.feature.schedule

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.api.feature.ScheduleApi
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.domain.repository.feature.ScheduleRepository

class RealScheduleRepository @Inject constructor(
    private val scheduleApi: ScheduleApi
) : ScheduleRepository {

    override suspend fun getSchedule(
        id: Long
    ): Result<List<Schedule>> {
        return scheduleApi.getSchedule(id).toDomain()
    }

    override suspend fun editSchedule(
        scheduleList: List<Schedule>
    ): Result<Unit> {
        return scheduleApi.editSchedule(scheduleList)
    }
}
