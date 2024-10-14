package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.entry

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.zip
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.term.GetTermListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProfileUseCase: GetProfileUseCase,
    private val getTermListUseCase: GetTermListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<RegisterEntryState> =
        MutableStateFlow(RegisterEntryState.Init)
    val state: StateFlow<RegisterEntryState> = _state.asStateFlow()

    private val _event: MutableEventFlow<RegisterEntryEvent> = MutableEventFlow()
    val event: EventFlow<RegisterEntryEvent> = _event.asEventFlow()

    init {
        launch {
            checkProgress()
        }
    }

    fun onIntent(intent: RegisterEntryIntent) {

    }

    private suspend fun checkProgress() {
        _state.value = RegisterEntryState.Loading

        zip(
            { getProfileUseCase() },
            { getTermListUseCase() }
        ).onSuccess { (profile, termList) ->
            _state.value = RegisterEntryState.Init

            val isNicknameValid = profile.nickname.isNotEmpty()
            val isTermAgreed = termList.all { term ->
                !term.isRequired || term.isAgree
            }

            when {
                !isTermAgreed -> {
                    _event.emit(RegisterEntryEvent.NeedTermAgreement)
                }

                !isNicknameValid -> {
                    _event.emit(RegisterEntryEvent.NeedNickname)
                }

                else -> {
                    _event.emit(RegisterEntryEvent.NoProblem)
                }
            }
        }.onFailure { exception ->
            _state.value = RegisterEntryState.Init
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
