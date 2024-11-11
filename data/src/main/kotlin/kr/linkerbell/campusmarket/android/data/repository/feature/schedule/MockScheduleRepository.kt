package kr.linkerbell.campusmarket.android.data.repository.feature.schedule

import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalTime
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.domain.repository.feature.ScheduleRepository

class MockScheduleRepository @Inject constructor() : ScheduleRepository {

    override suspend fun getSchedule(
        id: Long
    ): Result<List<Schedule>> {
        randomShortDelay()

        return Result.success(
            listOf(
                Schedule(
                    dayOfWeek = 1,
                    startTime = LocalTime(9, 0),
                    endTime = LocalTime(12, 0)
                ),
                Schedule(
                    dayOfWeek = 2,
                    startTime = LocalTime(9, 0),
                    endTime = LocalTime(12, 0)
                ),
                Schedule(
                    dayOfWeek = 3,
                    startTime = LocalTime(9, 0),
                    endTime = LocalTime(12, 0)
                ),
                Schedule(
                    dayOfWeek = 4,
                    startTime = LocalTime(9, 0),
                    endTime = LocalTime(12, 0)
                ),
                Schedule(
                    dayOfWeek = 5,
                    startTime = LocalTime(9, 0),
                    endTime = LocalTime(12, 0)
                )
            )
        )
    }

    override suspend fun editSchedule(
        scheduleList: List<Schedule>
    ): Result<Unit> {
        randomShortDelay()

        return Result.success(Unit)
    }

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
