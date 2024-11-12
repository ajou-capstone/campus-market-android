package kr.linkerbell.campusmarket.android.presentation.ui.main.home.schedule.compare

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.zip
import kr.linkerbell.campusmarket.android.domain.model.feature.schedule.Schedule
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.schedule.GetScheduleUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class ScheduleCompareViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getScheduleUseCase: GetScheduleUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ScheduleCompareState> =
        MutableStateFlow(ScheduleCompareState.Init)
    val state: StateFlow<ScheduleCompareState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ScheduleCompareEvent> = MutableEventFlow()
    val event: EventFlow<ScheduleCompareEvent> = _event.asEventFlow()

    val userId: Long by lazy {
        savedStateHandle.get<Long>(ScheduleCompareConstant.ROUTE_ARGUMENT_USER_ID) ?: -1L
    }

    private val _mySchedule: MutableStateFlow<List<Schedule>> = MutableStateFlow(emptyList())
    val mySchedule: StateFlow<List<Schedule>> = _mySchedule.asStateFlow()

    private val _userSchedule: MutableStateFlow<List<Schedule>> = MutableStateFlow(emptyList())
    val userSchedule: StateFlow<List<Schedule>> = _userSchedule.asStateFlow()

    init {
        launch {
            getMyProfileUseCase().mapCatching { myProfile ->
                zip(
                    { getScheduleUseCase(myProfile.id) },
                    { getScheduleUseCase(userId) }
                ).getOrThrow()
            }.onSuccess { (mySchedule, userSchedule) ->
                _mySchedule.value = mySchedule
                _userSchedule.value = userSchedule
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

    fun onIntent(intent: ScheduleCompareIntent) {

    }
}
