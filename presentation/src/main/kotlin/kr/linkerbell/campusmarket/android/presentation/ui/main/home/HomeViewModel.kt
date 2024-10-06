package kr.linkerbell.campusmarket.android.presentation.ui.main.home

import androidx.lifecycle.SavedStateHandle
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Init)
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _event: MutableEventFlow<HomeEvent> = MutableEventFlow()
    val event: EventFlow<HomeEvent> = _event.asEventFlow()

    val initialHomeType: HomeType by lazy {
        val route = savedStateHandle.get<String>(HomeConstant.ROUTE_ARGUMENT_SCREEN)
        HomeType.values().firstOrNull { it.route == route } ?: HomeType.values().first()
    }

    fun onIntent(intent: HomeIntent) {

    }
}
