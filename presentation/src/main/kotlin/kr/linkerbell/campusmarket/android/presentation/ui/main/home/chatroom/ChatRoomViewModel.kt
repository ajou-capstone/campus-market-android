package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.EventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.MutableEventFlow
import kr.linkerbell.campusmarket.android.common.util.coroutine.event.asEventFlow
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Session
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.ConnectRoomUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.GetMessageListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.GetRoomListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.QuitRoomUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.SetRoomNotificationUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getRoomListUseCase: GetRoomListUseCase,
    private val setRoomNotificationUseCase: SetRoomNotificationUseCase,
    private val quitRoomUseCase: QuitRoomUseCase,
    private val getMessageListUseCase: GetMessageListUseCase,
    private val connectRoomUseCase: ConnectRoomUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ChatRoomState> = MutableStateFlow(ChatRoomState.Init)
    val state: StateFlow<ChatRoomState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ChatRoomEvent> = MutableEventFlow()
    val event: EventFlow<ChatRoomEvent> = _event.asEventFlow()

    private val _roomList: MutableStateFlow<List<Room>> = MutableStateFlow(emptyList())
    val roomList: StateFlow<List<Room>> = _roomList.asStateFlow()

    private val _messageList: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messageList: StateFlow<List<Message>> = _messageList.asStateFlow()

    @OptIn(ObsoleteCoroutinesApi::class)
    private val sessionAction = viewModelScope.actor<ChatRoomIntent.Session>(coroutineContext) {
        var session: Session? = null

        suspend fun connect() {
            if (session != null) throw IllegalStateException("Session is already connected")

            connectRoomUseCase()
                .onSuccess {
                    session = it
                }.onFailure { exception ->
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

        suspend fun subscribe(
            id: Long
        ) {
            session?.let {
                launch {
                    it.subscribe(id).catch { exception ->
                        when (exception) {
                            is ServerException -> {
                                _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                            }

                            else -> {
                                _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                            }
                        }
                    }.collect()
                }
            } ?: throw IllegalStateException("Session is not connected")
        }

        suspend fun disconnect() {
            session?.let {
                it.disconnect()
                session = null
            } ?: throw IllegalStateException("Session is not connected")
        }

        consumeEach { intent ->
            when (intent) {
                ChatRoomIntent.Session.Connect -> {
                    connect()
                }

                is ChatRoomIntent.Session.Subscribe -> {
                    subscribe(
                        id = intent.id
                    )
                }

                ChatRoomIntent.Session.Disconnect -> {
                    disconnect()
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

            is ChatRoomIntent.QuitRoom -> {
                quitRoom(
                    id = intent.id,
                )
            }

            ChatRoomIntent.Refresh -> {
                refresh()
            }

            is ChatRoomIntent.Session -> {
                launch {
                    sessionAction.send(intent)
                }
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

    private fun quitRoom(id: Long) {
        launch {
            quitRoomUseCase(
                id = id
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

    private fun refresh() {
        launch {
            onIntent(ChatRoomIntent.Session.Connect)
            getRoomListUseCase()
                .catch { exception ->
                    when (exception) {
                        is ServerException -> {
                            _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                        }

                        else -> {
                            _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                        }
                    }
                    _roomList.value = emptyList()
                }.collect { roomList ->
                    _state.value = ChatRoomState.Init
                    val newRoomId: Set<Long> =
                        roomList.map { it.id }.toSet() - _roomList.value.map { it.id }.toSet()
                    newRoomId.forEach { id ->
                        onIntent(ChatRoomIntent.Session.Subscribe(id))
                    }
                    _roomList.value = roomList
                }
        }
        launch {
            getMessageListUseCase(roomId = -1)
                .catch { exception ->
                    when (exception) {
                        is ServerException -> {
                            _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                        }

                        else -> {
                            _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                        }
                    }
                    _messageList.value = emptyList()
                }.collect {
                    _messageList.value = it
                }
        }
    }
}
