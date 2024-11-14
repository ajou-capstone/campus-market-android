package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.usecase.feature.schedule.EditScheduleUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.schedule.GetScheduleUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getScheduleUseCase: GetScheduleUseCase,
    private val editScheduleUseCase: EditScheduleUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ScheduleState> = MutableStateFlow(ScheduleState.Init)
    val state: StateFlow<ScheduleState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ScheduleEvent> = MutableEventFlow()
    val event: EventFlow<ScheduleEvent> = _event.asEventFlow()

    private val _myProfile: MutableStateFlow<MyProfile> = MutableStateFlow(MyProfile.empty)
    val myProfile: StateFlow<MyProfile> = _myProfile.asStateFlow()

    private val _scheduleList: MutableStateFlow<List<Schedule>> = MutableStateFlow(emptyList())
    val scheduleList: StateFlow<List<Schedule>> = _scheduleList.asStateFlow()

    init {
        launch {
            _state.value = ScheduleState.Loading
            getMyProfileUseCase()
                .mapCatching { myProfile ->
                    _myProfile.value = myProfile
                    getScheduleUseCase(
                        id = myProfile.id
                    ).getOrThrow()
                }.onSuccess { scheduleList ->
                    _scheduleList.value = scheduleList
                }.onFailure { exception ->
                    _state.value = ScheduleState.Init
                    when (exception) {
                        is ServerException -> {
                            _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                        }

                        else -> {
                            _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                        }
                    }
                }
        }
    }

    fun onIntent(intent: ScheduleIntent) {
        when (intent) {
            is ScheduleIntent.AddSchedule -> {
                addSchedule(
                    schedule = intent.schedule
                )
            }

            is ScheduleIntent.RemoveSchedule -> {
                removeSchedule(
                    schedule = intent.schedule
                )
            }

            is ScheduleIntent.UpdateSchedule -> {
                updateSchedule(
                    from = intent.from,
                    to = intent.to
                )
            }
        }
    }

    private fun addSchedule(
        schedule: Schedule
    ) {
        launch {
            val scheduleList = scheduleList.value
            val duplicateScheduleList: List<Schedule> = scheduleList.filter {
                if (it.dayOfWeek != schedule.dayOfWeek) return@filter false

                it.endTime > schedule.startTime && it.startTime < schedule.endTime
            }
            val newScheduleList = scheduleList - duplicateScheduleList.toSet() + schedule
            editScheduleUseCase(
                scheduleList = newScheduleList
            ).onSuccess {
                _scheduleList.value = newScheduleList
            }.onFailure { exception ->
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }
        }
    }

    private fun removeSchedule(
        schedule: Schedule
    ) {
        launch {
            val newScheduleList = scheduleList.value - schedule
            editScheduleUseCase(
                scheduleList = newScheduleList
            ).onSuccess {
                _scheduleList.value = newScheduleList
            }.onFailure { exception ->
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }
        }
    }

    private fun updateSchedule(
        from: Schedule,
        to: Schedule
    ) {
        launch {
            val scheduleList = scheduleList.value - from
            val duplicateScheduleList: List<Schedule> = scheduleList.filter {
                if (it.dayOfWeek != to.dayOfWeek) return@filter false

                it.endTime > to.startTime && it.startTime < to.endTime
            }
            val newScheduleList = scheduleList - duplicateScheduleList.toSet() + to
            editScheduleUseCase(
                scheduleList = newScheduleList
            ).onSuccess {
                _scheduleList.value = newScheduleList
            }.onFailure { exception ->
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }
        }
    }
}
