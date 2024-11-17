package kr.linkerbell.campusmarket.android.presentation.ui.main.home.chatroom.chat

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
import kr.linkerbell.campusmarket.android.domain.model.feature.trade.TradeInfo
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.error.ServerException
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.MyProfile
import kr.linkerbell.campusmarket.android.domain.model.nonfeature.user.UserProfile
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.ConnectRoomUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.GetMessageListUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.GetRoomUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.chat.ReadMessageUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.ChangeTradeStatusUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.feature.trade.GetTradeInfoUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.GetPreSignedUrlUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.file.UploadImageUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetMyProfileUseCase
import kr.linkerbell.campusmarket.android.domain.usecase.nonfeature.user.GetUserProfileUseCase
import kr.linkerbell.campusmarket.android.presentation.common.base.BaseViewModel
import kr.linkerbell.campusmarket.android.presentation.common.base.ErrorEvent
import kr.linkerbell.campusmarket.android.presentation.model.gallery.GalleryImage

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val connectRoomUseCase: ConnectRoomUseCase,
    private val getRoomUseCase: GetRoomUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getMessageListUseCase: GetMessageListUseCase,
    private val readMessageUseCase: ReadMessageUseCase,
    private val getPreSignedUrlUseCase: GetPreSignedUrlUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val changeTradeStatusUseCase: ChangeTradeStatusUseCase,
    private val getTradeInfoUseCase: GetTradeInfoUseCase
) : BaseViewModel() {

    private val _state: MutableStateFlow<ChatState> = MutableStateFlow(ChatState.Init)
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _event: MutableEventFlow<ChatEvent> = MutableEventFlow()
    val event: EventFlow<ChatEvent> = _event.asEventFlow()

    val roomId: Long by lazy {
        savedStateHandle.get<Long>(ChatConstant.ROUTE_ARGUMENT_ROOM_ID) ?: -1L
    }

    private val _messageList: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messageList: StateFlow<List<Message>> = _messageList.asStateFlow()

    private val _userProfile: MutableStateFlow<UserProfile> = MutableStateFlow(UserProfile.empty)
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _myProfile: MutableStateFlow<MyProfile> = MutableStateFlow(MyProfile.empty)
    val myProfile: StateFlow<MyProfile> = _myProfile.asStateFlow()

    private val _room: MutableStateFlow<Room> = MutableStateFlow(Room.empty)
    val room: StateFlow<Room> = _room.asStateFlow()

    private val _trade: MutableStateFlow<TradeInfo> = MutableStateFlow(TradeInfo.empty)
    val trade: StateFlow<TradeInfo> = _trade.asStateFlow()

    @OptIn(ObsoleteCoroutinesApi::class)
    private val sessionAction = viewModelScope.actor<ChatIntent.Session>(coroutineContext) {
        var session: Session? = null

        suspend fun connect() {
            if (session != null) throw IllegalStateException("Session is already connected")

            var retryCount = 0
            do {
                val result = connectRoomUseCase()
                    .onSuccess {
                        session = it
                    }.onFailure {
                        retryCount++
                    }

                if (retryCount >= 100) {
                    result.onFailure { exception ->
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
            } while (result.isFailure && retryCount < 100)
        }

        suspend fun subscribe(
            id: Long
        ) {
            session?.let {
                // TODO : Refresh / Unsubscribe 등의 구독 취소 시 Cancel 로직 없음
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

        suspend fun sendText(text: String) {
            session?.let {
                it.send(roomId, text, "TEXT").onFailure { exception ->
                    when (exception) {
                        is ServerException -> {
                            _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                        }

                        else -> {
                            _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                        }
                    }
                }
            } ?: throw IllegalStateException("Session is not connected")
        }

        suspend fun sendImage(image: GalleryImage) {
            session?.let {
                getPreSignedUrlUseCase(
                    fileName = image.name
                ).mapCatching { preSignedUrl ->
                    uploadImageUseCase(
                        imageUri = image.filePath,
                        preSignedUrl = preSignedUrl.preSignedUrl
                    ).getOrThrow()

                    preSignedUrl.s3url
                }.onSuccess { url ->
                    it.send(roomId, url, "IMAGE")
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
            } ?: throw IllegalStateException("Session is not connected")
        }

        suspend fun sendSchedule() {
            session?.let {
                it.send(roomId, "", "TIMETABLE")
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
                ChatIntent.Session.Connect -> {
                    connect()
                }

                is ChatIntent.Session.Subscribe -> {
                    subscribe(
                        id = roomId
                    )
                }

                is ChatIntent.Session.SendText -> {
                    sendText(text = intent.text)
                }

                is ChatIntent.Session.SendImage -> {
                    sendImage(image = intent.image)
                }

                is ChatIntent.Session.SendSchedule -> {
                    sendSchedule()
                }

                ChatIntent.Session.Disconnect -> {
                    disconnect()
                }
            }
        }
    }

    fun onIntent(intent: ChatIntent) {
        when (intent) {
            ChatIntent.Refresh -> {
                refresh()
            }

            is ChatIntent.Session -> {
                launch {
                    sessionAction.send(intent)
                }
            }

            ChatIntent.OnSell -> {
                sell()
            }
        }
    }

    private fun refresh() {
        launch {
            onIntent(ChatIntent.Session.Connect)
            onIntent(ChatIntent.Session.Subscribe)
            getRoomUseCase(
                id = roomId
            ).catch { exception ->
                when (exception) {
                    is ServerException -> {
                        _errorEvent.emit(ErrorEvent.InvalidRequest(exception))
                    }

                    else -> {
                        _errorEvent.emit(ErrorEvent.UnavailableServer(exception))
                    }
                }
            }.collect {
                _room.value = it

                if (userProfile.value == UserProfile.empty) {
                    getUserProfileUseCase(
                        id = it.userId
                    ).onSuccess {
                        _userProfile.value = it
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

                if (trade.value == TradeInfo.empty) {
                    getTradeInfoUseCase(
                        itemId = it.tradeId
                    ).onSuccess {
                        _trade.value = it
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

                if (myProfile.value == MyProfile.empty) {
                    getMyProfileUseCase()
                        .onSuccess {
                            _myProfile.value = it
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
            }
        }
        launch {
            getMessageListUseCase(roomId = roomId)
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

    private fun sell() {
        launch {
            changeTradeStatusUseCase(
                itemStatus = "SOLDOUT",
                itemId = room.value.tradeId,
                buyerId = userProfile.value.id,
            ).onSuccess {
                _event.emit(ChatEvent.Sell.Success)
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
    }
}
