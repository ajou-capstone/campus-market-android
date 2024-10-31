package kr.linkerbell.campusmarket.android.data.repository.feature.chat

import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Session
import kr.linkerbell.campusmarket.android.domain.repository.feature.ChatRepository

class MockChatRepository @Inject constructor() : ChatRepository {

    override fun getRoomList(): Flow<List<Room>> {
        return flow {
            randomShortDelay()

            emit(
                listOf(
                    Room(
                        id = 1L,
                        userId = 1L,
                        tradeId = 1L,
                        title = "title1",
                        isAlarm = true,
                        readLatestMessageId = 1L
                    ),
                    Room(
                        id = 2L,
                        userId = 2L,
                        tradeId = 2L,
                        title = "title2",
                        isAlarm = false,
                        readLatestMessageId = 2L
                    )
                )
            )
        }
    }

    override suspend fun quitRoom(
        id: Long
    ): Result<Unit> {
        randomShortDelay()

        return Result.success(Unit)
    }

    override suspend fun makeRoom(
        userId: Long,
        tradeId: Long
    ): Result<Room> {
        randomShortDelay()

        return Result.success(
            Room(
                id = 1L,
                userId = 1L,
                tradeId = 1L,
                title = "title1",
                isAlarm = true,
                readLatestMessageId = 1L
            )
        )
    }

    override suspend fun setRoomNotification(
        id: Long,
        isAlarm: Boolean
    ): Result<Unit> {
        randomShortDelay()

        return Result.success(Unit)
    }

    override fun getMessageList(): Flow<List<Message>> {
        return flow {
            randomShortDelay()

            emit(
                listOf(
                    Message.Text(
                        id = 1L,
                        chatRoomId = 1L,
                        userId = 1L,
                        content = "안녕하세요.",
                        createdAt = System.currentTimeMillis()
                    ),
                    Message.Image(
                        id = 2L,
                        chatRoomId = 1L,
                        userId = 1L,
                        content = "https://placehold.co/600x400",
                        createdAt = System.currentTimeMillis()
                    ),
                )
            )
        }
    }

    override suspend fun readMessage(
        id: Long
    ): Result<Unit> {
        randomShortDelay()

        return Result.success(Unit)
    }

    override suspend fun connectRoom(
        id: Long
    ): Result<Session> {
        randomShortDelay()

        return Result.success(
            Session(
                subscribe = { flow { } },
                send = { },
                disconnect = { }
            )
        )
    }

    private suspend fun randomShortDelay() {
        delay(LongRange(100, 500).random())
    }

    private suspend fun randomLongDelay() {
        delay(LongRange(500, 2000).random())
    }
}
