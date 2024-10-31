package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.login

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
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication.LoginUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Init)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _event: MutableEventFlow<LoginEvent> = MutableEventFlow()
    val event: EventFlow<LoginEvent> = _event.asEventFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> {
                login(
                    idToken = intent.idToken
                )
            }
        }
    }

    private fun login(idToken: String) {
        launch {
            _state.value = LoginState.Loading

            loginUseCase(
                idToken = idToken
            ).onSuccess {
                _event.emit(LoginEvent.Login.Success)
            }.onFailure { exception ->
                _state.value = LoginState.Init
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
