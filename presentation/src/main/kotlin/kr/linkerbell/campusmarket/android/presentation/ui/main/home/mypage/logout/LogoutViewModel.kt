package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout

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
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication.LogoutUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<LogoutState> = MutableStateFlow(LogoutState.Init)
    val state: StateFlow<LogoutState> = _state.asStateFlow()

    private val _event: MutableEventFlow<LogoutEvent> = MutableEventFlow()
    val event: EventFlow<LogoutEvent> = _event.asEventFlow()

    fun onIntent(intent: LogoutIntent) {
        when (intent) {
            is LogoutIntent.LogOut -> {
                launch {
                    userLogOut()
                }
            }
        }
    }

    private suspend fun userLogOut() {
        logoutUseCase().onSuccess {
            _state.value = LogoutState.Init
        }.onFailure { exception ->
            _state.value = LogoutState.Init
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
