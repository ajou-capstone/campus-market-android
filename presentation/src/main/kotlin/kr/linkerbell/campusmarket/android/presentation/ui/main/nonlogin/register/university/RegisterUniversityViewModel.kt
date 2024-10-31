package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.university

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication.SendEmailVerifyCodeUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.authentication.VerifyEmailVerifyCodeUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel

@HiltViewModel
class RegisterUniversityViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val sendEmailVerifyCodeUseCase: SendEmailVerifyCodeUseCase,
    private val verifyEmailVerifyCodeUseCase: VerifyEmailVerifyCodeUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<RegisterUniversityState> =
        MutableStateFlow(RegisterUniversityState.Init)
    val state: StateFlow<RegisterUniversityState> = _state.asStateFlow()

    private val _event: MutableEventFlow<RegisterUniversityEvent> = MutableEventFlow()
    val event: EventFlow<RegisterUniversityEvent> = _event.asEventFlow()

    private var token: String
        get() = savedStateHandle.get<String>(KEY_TOKEN).orEmpty()
        set(value) {
            savedStateHandle[KEY_TOKEN] = value
        }

    fun onIntent(intent: RegisterUniversityIntent) {
        when (intent) {
            is RegisterUniversityIntent.OnSendEmail -> {
                sendEmailVerifyCode(
                    email = intent.email
                )
            }

            is RegisterUniversityIntent.OnConfirm -> {
                verifyEmailVerifyCode(
                    verifyCode = intent.verifyCode
                )
            }
        }
    }

    private fun sendEmailVerifyCode(
        email: String
    ) {
        launch {
            sendEmailVerifyCodeUseCase(
                email = email
            ).onSuccess {
                token = it
                _event.emit(RegisterUniversityEvent.CheckEmail.Success)
            }.onFailure { exception ->
                _event.emit(RegisterUniversityEvent.CheckEmail.Failure)
            }
        }
    }

    private fun verifyEmailVerifyCode(
        verifyCode: String
    ) {
        launch {
            verifyEmailVerifyCodeUseCase(
                token = token,
                verifyCode = verifyCode
            ).onSuccess {
                _event.emit(RegisterUniversityEvent.CheckVerifyCode.Success)
            }.onFailure { exception ->
                _event.emit(RegisterUniversityEvent.CheckVerifyCode.Failure)
            }
        }
    }

    companion object {
        private const val KEY_TOKEN = "key_token"
    }
}
