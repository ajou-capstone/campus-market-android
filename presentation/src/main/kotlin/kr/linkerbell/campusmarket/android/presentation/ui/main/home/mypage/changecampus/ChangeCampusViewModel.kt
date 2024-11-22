package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.changecampus

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
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
import javax.inject.Inject

@HiltViewModel
class ChangeCampusViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAvailableCampusListUseCase: GetAvailableCampusListUseCase,
    private val setCampusUseCase: SetCampusUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ChangeCampusState> =
        MutableStateFlow(ChangeCampusState.Init)
    val state: StateFlow<ChangeCampusState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ChangeCampusEvent> = MutableEventFlow()
    val event: EventFlow<ChangeCampusEvent> = _event.asEventFlow()

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

    fun onIntent(intent: ChangeCampusIntent) {
        when (intent) {
            is ChangeCampusIntent.OnConfirm -> {
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
                _event.emit(ChangeCampusEvent.SetCampus.Success)
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
