package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.GetMessageListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.GetRoomListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.SetRoomNotificationUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetUserProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRoomListUseCase: GetRoomListUseCase,
    private val setRoomNotificationUseCase: SetRoomNotificationUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getMessageListUseCase: GetMessageListUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ChatRoomState> = MutableStateFlow(ChatRoomState.Init)
    val state: StateFlow<ChatRoomState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ChatRoomEvent> = MutableEventFlow()
    val event: EventFlow<ChatRoomEvent> = _event.asEventFlow()

    private val _roomList: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())
    val roomList: StateFlow<List<Room>> = _roomList.asStateFlow()

    private val _userProfileList: MutableStateFlow<List<UserProfile>> =
        MutableStateFlow(emptyList())
    val userProfileList: StateFlow<List<UserProfile>> = _userProfileList.asStateFlow()

    private val _messageList: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messageList: StateFlow<List<Message>> = _messageList.asStateFlow()

    init {
        launch {
            getRoomListUseCase().catch {
                _roomList.value = emptyList()
            }.collect { roomList ->
                _state.value = ChatRoomState.Init
                _roomList.value = roomList

                _userProfileList.value = roomList.filter { room ->
                    userProfileList.value.none { userProfile ->
                        room.id == userProfile.id
                    }
                }.mapNotNull { room ->
                    getUserProfileUseCase(
                        id = room.id
                    ).getOrNull()
                }
            }
        }
    }

    fun onIntent(intent: ChatRoomIntent) {
        when (intent) {
            is ChatRoomIntent.SetRoomNotification -> {
                setRoomNotification(
                    id = intent.id,
                    isNotification = intent.isNotification
                )
            }
        }
    }

    private fun setRoomNotification(
        id: Long,
        isNotification: Boolean
    ) {
        launch {
            setRoomNotificationUseCase(
                id = id,
                isAlarm = isNotification
            ).onFailure { exception ->
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
