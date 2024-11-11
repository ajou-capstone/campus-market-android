package kr.linkerbell.campusmarket.android.domain.usecase.feature.schedule

import javax.inject.Inject
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.domain.repository.feature.ScheduleRepository

class EditScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(
        scheduleList: List<Schedule>
    ): Result<Unit> {
        return scheduleRepository.editSchedule(
            scheduleList = scheduleList
        )
    }
}
