package kr.linkerbell.campusmarket.android.presentation.ui.main.home.mypage.logout.withdrawal

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val _state: MutableStateFlow<WithdrawalState> = MutableStateFlow(WithdrawalState.Init)
    val state: StateFlow<WithdrawalState> = _state.asStateFlow()

    private val _event: MutableEventFlow<WithdrawalEvent> = MutableEventFlow()
    val event: EventFlow<WithdrawalEvent> = _event.asEventFlow()

    fun onIntent(intent: WithdrawalIntent) {

    }
}
