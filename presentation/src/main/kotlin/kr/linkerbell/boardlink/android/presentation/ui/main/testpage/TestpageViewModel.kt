package kr.linkerbell.boardlink.android.presentation.ui.main.testpage

import kr.linkerbell.boardlink.android.presentation.common.base.BaseViewModel
import kr.linkerbell.boardlink.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.boardlink.android.common.util.coroutine.event.asEventFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlinx.coroutines.launch
import kr.linkerbell.boardlink.android.presentation.ui.main.testpage.testpageData.DayOfTheWeek
import kr.linkerbell.boardlink.android.presentation.ui.main.testpage.testpageData.UserCalender

@HiltViewModel
class TestpageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    private val _state: MutableStateFlow<testpageState> = MutableStateFlow(testpageState.Init)
    val state: StateFlow<testpageState> = _state.asStateFlow()

    private val _event: MutableEventFlow<testpageEvent> = MutableEventFlow()
    val event: EventFlow<testpageEvent> = _event.asEventFlow()


    private val _userCalender = MutableStateFlow<UserCalender?>(null)
    val userCalender: StateFlow<UserCalender?> = _userCalender

    init {
        viewModelScope.launch {
            fetchUserCalender()
        }
    }

    // 대충 User ID 기반으로 API 요청 -> UserCalender 받아온 것으로 간주
    // 실제 구현 시에는 (id: Int, 학기 정보) 인수 받아서 처리
    private suspend fun fetchUserCalender() {
        try {
            val tmpUserCalender = UserCalender("홍길동", "2024-1")
            tmpUserCalender.addBlock(9, 0, DayOfTheWeek.MON)
            tmpUserCalender.addBlock(9, 1, DayOfTheWeek.MON)
            tmpUserCalender.addBlock(11, 0, DayOfTheWeek.TUE)
            tmpUserCalender.addBlock(11, 0, DayOfTheWeek.TUE)
            tmpUserCalender.addBlock(13, 1, DayOfTheWeek.TUE)
            tmpUserCalender.addBlock(14, 0, DayOfTheWeek.TUE)
            tmpUserCalender.addBlock(14, 1, DayOfTheWeek.TUE)

            _state.value = testpageState.Loaded(tmpUserCalender)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun onIntent(intent: testpageIntent) {

    }

}
