package kr.linkerbell.boardlink.android.presentation.ui.main.setting

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.boardlink.android.presentation.common.base.BaseViewModel

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val _state: MutableStateFlow<SettingState> = MutableStateFlow(SettingState.Init)
    val state: StateFlow<SettingState> = _state.asStateFlow()

    private val _event: MutableEventFlow<SettingEvent> = MutableEventFlow()
    val event: EventFlow<SettingEvent> = _event.asEventFlow()

    fun onIntent(intent: SettingIntent) {

    }
}
