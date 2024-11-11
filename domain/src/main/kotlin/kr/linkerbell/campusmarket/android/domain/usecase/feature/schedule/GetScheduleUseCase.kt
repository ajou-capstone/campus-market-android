package kr.linkerbell.campusmarket.android.domain.usecase.feature.schedule

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.domain.repository.feature.ScheduleRepository

class GetScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(
        id: Long
    ): Result<List<Schedule>> {
        return scheduleRepository.getSchedule(
            id = id
        )
    }
}
