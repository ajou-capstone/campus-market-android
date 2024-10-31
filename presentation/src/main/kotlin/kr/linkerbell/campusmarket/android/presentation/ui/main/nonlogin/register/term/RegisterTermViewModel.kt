package kr.linkerbell.campusmarket.android.presentation.ui.main.nonlogin.register.term

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
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.term.Term
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.term.AgreeTermListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.term.GetTermListUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class RegisterTermViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTermListUseCase: GetTermListUseCase,
    private val agreeTermListUseCase: AgreeTermListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<RegisterTermState> =
        MutableStateFlow(RegisterTermState.Init)
    val state: StateFlow<RegisterTermState> = _state.asStateFlow()

    private val _event: MutableEventFlow<RegisterTermEvent> = MutableEventFlow()
    val event: EventFlow<RegisterTermEvent> = _event.asEventFlow()

    private val _termList: MutableStateFlow<List<Term>> = MutableStateFlow(emptyList())
    val termList: StateFlow<List<Term>> = _termList.asStateFlow()

    init {
        launch {
            getTermList()
        }
    }

    fun onIntent(intent: RegisterTermIntent) {
        when (intent) {
            is RegisterTermIntent.OnConfirm -> {
                agreeTermState(intent.checkedTermList)
            }
        }
    }

    private suspend fun getTermList() {
        getTermListUseCase().onSuccess { termList ->
            _termList.value = termList.filter { term ->
                !term.isAgree
            }
        }.onFailure { exception ->
            _state.value = RegisterTermState.Init
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

    private fun agreeTermState(
        checkedTermList: List<Long>
    ) {
        launch {
            _state.value = RegisterTermState.Loading
            agreeTermListUseCase(
                termIdList = checkedTermList
            ).onSuccess {
                _event.emit(RegisterTermEvent.AgreeTerm.Success)
                _state.value = RegisterTermState.Init
            }.onFailure { exception ->
                _state.value = RegisterTermState.Init
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
