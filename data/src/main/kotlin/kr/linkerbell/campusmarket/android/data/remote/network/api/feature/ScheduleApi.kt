package kr.linkerbell.campusmarket.android.data.remote.network.api.feature

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.schedule.EditScheduleItemReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.schedule.EditScheduleReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.schedule.GetScheduleRes
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule

class ScheduleApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun getSchedule(
        id: Long
    ): Result<GetScheduleRes> {
        return client.get("$baseUrl/api/v1/timetable/$id")
            .convert(errorMessageMapper::map)
    }

    suspend fun editSchedule(
        scheduleList: List<Schedule>
    ): Result<Unit> {
        return client.patch("$baseUrl/api/v1/timetable") {
            setBody(
                EditScheduleReq(
                    timetable = scheduleList.map {
                        EditScheduleItemReq(
                            dayOfWeek = it.dayOfWeek,
                            startTime = it.startTime,
                            endTime = it.endTime
                        )
                    }
                )
            )
        }.convert(errorMessageMapper::map)
    }
}
