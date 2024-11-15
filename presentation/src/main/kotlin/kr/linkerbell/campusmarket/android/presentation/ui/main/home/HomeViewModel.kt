package kr.linkerbell.campusmarket.android.presentation.ui.main.home

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.schedule.GetScheduleUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getScheduleUseCase: GetScheduleUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Init)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _event: MutableEventFlow<HomeEvent> = MutableEventFlow()
    val event: EventFlow<HomeEvent> = _event.asEventFlow()

    val initialHomeType: HomeType by lazy {
        val route = savedStateHandle.get<String>(HomeConstant.ROUTE_ARGUMENT_SCREEN)
        HomeType.values().firstOrNull { it.route == route } ?: HomeType.values().first()
    }

    init {
        launch {
            getMyProfileUseCase()
                .mapCatching { myProfile ->
                    getScheduleUseCase(
                        id = myProfile.id
                    ).getOrThrow()
                }.onSuccess { scheduleList ->
                    if (scheduleList.isEmpty()) {
                        _event.emit(HomeEvent.NeedSchedule)
                    }
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

    fun onIntent(intent: HomeIntent) {

    }
}
