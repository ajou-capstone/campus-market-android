package kr.linkerbell.campusmarket.android.data.repository.feature.chat

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.linkerbell.campusmarket.android.data.remote.local.database.message.MessageDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.message.toEntity
import kr.linkerbell.campusmarket.android.data.remote.local.database.room.RoomDao
import kr.linkerbell.campusmarket.android.data.remote.local.database.room.toEntity
import kr.linkerbell.campusmarket.android.data.remote.network.api.feature.ChatApi
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.MessageRes
import kr.linkerbell.campusmarket.android.data.remote.network.model.feature.chat.toRequest
import kr.linkerbell.campusmarket.android.data.remote.network.util.toDomain
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Session
import kr.linkerbell.campusmarket.android.domain.repository.feature.ChatRepository
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribe

class RealChatRepository @Inject constructor(
    private val chatApi: ChatApi,
    private val messageDao: MessageDao,
    private val roomDao: RoomDao,
    private val json: Json
) : ChatRepository {

    override fun getRoomList(): Flow<List<Room>> {
        return roomDao.getAll().map { roomList ->
            roomList.map { room ->
                room.toDomain()
            }
        }.onStart {
            val roomEntityList = chatApi.getRoomList()
                .toDomain()
                .getOrThrow()
                .map { room ->
                    room.toEntity()
                }

            roomDao.insert(*roomEntityList.toTypedArray())
        }
    }

    override suspend fun quitRoom(
        id: Long
    ): Result<Unit> {
        return chatApi.quitRoom(
            id = id
        ).onSuccess {
            roomDao.delete(id = id)
        }
    }

    override suspend fun makeRoom(
        userId: Long,
        tradeId: Long
    ): Result<Room> {
        return chatApi.makeRoom(
            userId = userId,
            tradeId = tradeId
        ).toDomain().onSuccess {
            roomDao.insert(it.toEntity())
        }
    }

    override suspend fun setRoomNotification(
        id: Long,
        isAlarm: Boolean
    ): Result<Unit> {
        return chatApi.setRoomNotification(
            id = id,
            isAlarm = isAlarm
        ).onSuccess {
            roomDao.get(id = id).firstOrNull()?.let {
                roomDao.update(it.copy(isAlarm = isAlarm))
            }
        }
    }

    override fun getMessageList(): Flow<List<Message>> {
        return messageDao.getAll().map { roomList ->
            roomList.map { room ->
                room.toDomain()
            }
        }.onStart {
            val messageIdList = chatApi.getRecentMessageIdList().toDomain().getOrThrow()
            val missingIdList = messageDao.findMissingIdList(idList = messageIdList)
            val messageList = chatApi.getMessageListById(idList = missingIdList)
                .toDomain()
                .getOrThrow()
                .map { room -> room.toEntity() }
            messageDao.insert(*messageList.toTypedArray())
        }
    }

    override suspend fun readMessage(
        id: Long
    ): Result<Unit> {
        return chatApi.readMessage(
            id = id
        ).onSuccess {
            val message = messageDao.get(id = id) ?: return@onSuccess
            val room = roomDao.get(id = message.roomId).firstOrNull() ?: return@onSuccess

            roomDao.update(room.copy(readLatestMessageId = id))
        }
    }

    override suspend fun connectRoom(
        id: Long
    ): Result<Session> {
        return chatApi.connectRoom().map { stompSession ->
            Session(
                subscribe = {
                    stompSession.subscribe("/sub/chat/$id").map {
                        json.decodeFromString<MessageRes>(it.bodyAsText).toDomain()
                    }
                },
                send = { message ->
                    stompSession.sendText(
                        destination = "/send/chat/$id",
                        body = json.encodeToString(message.toRequest())
                    )
                },
                disconnect = {
                    stompSession.disconnect()
                }
            )
        }
    }
}
