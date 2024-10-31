package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.campus

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
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.Campus
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetAvailableCampusListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.SetCampusUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class RegisterCampusViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAvailableCampusListUseCase: GetAvailableCampusListUseCase,
    private val setCampusUseCase: SetCampusUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<RegisterCampusState> =
        MutableStateFlow(RegisterCampusState.Init)
    val state: StateFlow<RegisterCampusState> = _state.asStateFlow()

    private val _event: MutableEventFlow<RegisterCampusEvent> = MutableEventFlow()
    val event: EventFlow<RegisterCampusEvent> = _event.asEventFlow()

    private val _campusList: MutableStateFlow<List<Campus>> = MutableStateFlow(emptyList())
    val campusList: StateFlow<List<Campus>> = _campusList.asStateFlow()

    init {
        launch {
            getAvailableCampusListUseCase().onSuccess {
                _campusList.value = it
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

    fun onIntent(intent: RegisterCampusIntent) {
        when (intent) {
            is RegisterCampusIntent.OnConfirm -> {
                setCampus(
                    id = intent.id
                )
            }
        }
    }

    private fun setCampus(
        id: Long
    ) {
        launch {
            setCampusUseCase(
                id = id
            ).onSuccess {
                _event.emit(RegisterCampusEvent.SetCampus.Success)
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
