package kr.linkerbell.campusmarket.android.data.remote.network.api.feature

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject
import kr.linkerbell.campusmarket.android.data.remote.network.di.AuthHttpClient
import kr.linkerbell.campusmarket.android.data.remote.network.environment.BaseUrlProvider
import kr.linkerbell.campusmarket.android.data.remote.network.environment.ErrorMessageMapper
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.GetMessageListByIdReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.GetMessageListByIdRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.GetRecentMessageIdListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.GetRoomListRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.GetRoomRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.MakeRoomReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.MakeRoomRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.ReadMessageReq
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.SetRoomNotificationReq
import kr.linkerbell.campusmarket.android.data.remote.network.util.convert
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import timber.log.Timber

class ChatApi @Inject constructor(
    @AuthHttpClient private val client: HttpClient,
    private val stompClient: StompClient,
    private val baseUrlProvider: BaseUrlProvider,
    private val errorMessageMapper: ErrorMessageMapper
) {
    private val baseUrl: String
        get() = baseUrlProvider.get()

    suspend fun getRoomList(): Result<GetRoomListRes> {
        return client.get("$baseUrl/api/v1/chat/rooms")
            .convert(errorMessageMapper::map)
    }

    suspend fun getRoom(
        id: Long
    ): Result<GetRoomRes> {
        return client.get("$baseUrl/api/v1/chat/room/$id")
            .convert(errorMessageMapper::map)
    }

    suspend fun quitRoom(
        id: Long
    ): Result<Unit> {
        return client.patch("$baseUrl/api/v1/chat/$id")
            .convert(errorMessageMapper::map)
    }

    suspend fun makeRoom(
        userId: Long,
        tradeId: Long
    ): Result<MakeRoomRes> {
        return client.post("$baseUrl/api/v1/chat") {
            setBody(
                MakeRoomReq(
                    userId = userId,
                    tradeId = tradeId
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun setRoomNotification(
        id: Long,
        isAlarm: Boolean
    ): Result<Unit> {
        return client.patch("$baseUrl/api/v1/chat/$id/alarm") {
            setBody(
                SetRoomNotificationReq(
                    isAlarm = isAlarm
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun getRecentMessageIdList(): Result<GetRecentMessageIdListRes> {
        return client.get("$baseUrl/api/v1/chat/recent-message")
            .convert(errorMessageMapper::map)
    }

    suspend fun readMessage(
        id: Long
    ): Result<Unit> {
        return client.patch("$baseUrl/api/v1/chat/read-message") {
            setBody(
                ReadMessageReq(
                    id = id
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun getMessageListById(
        idList: List<Long>
    ): Result<GetMessageListByIdRes> {
        return client.post("$baseUrl/api/v1/chat/message") {
            setBody(
                GetMessageListByIdReq(
                    messageIdList = idList
                )
            )
        }.convert(errorMessageMapper::map)
    }

    suspend fun connectRoom(): Result<StompSession> {
        return runCatching {
            stompClient.connect(
                url = "$baseUrl/ws/chat".replaceFirst("https", "wss")
            )
        }.onFailure { exception ->
            Timber.d(exception)
            Firebase.crashlytics.recordException(exception)
        }
    }
}
