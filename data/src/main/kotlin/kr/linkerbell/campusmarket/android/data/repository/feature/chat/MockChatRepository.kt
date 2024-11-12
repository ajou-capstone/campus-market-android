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

    override fun getRoom(
        id: Long
    ): Flow<Room> {
        return flow {
            randomShortDelay()

            emit(
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

    override fun getMessageList(
        roomId: Long
    ): Flow<List<Message>> {
        return flow {
            randomShortDelay()

            val currentTime = System.currentTimeMillis()
            emit(
                listOf(
                    Message.Schedule(
                        id = 0L,
                        chatRoomId = 1L,
                        userId = 1L,
                        createdAt = currentTime - 6L
                    ),
                    Message.Text(
                        id = 1L,
                        chatRoomId = 1L,
                        userId = 1L,
                        content = "안녕하세요.",
                        createdAt = currentTime - 5L
                    ),
                    Message.Image(
                        id = 2L,
                        chatRoomId = 1L,
                        userId = 1L,
                        content = "https://placehold.co/600x400",
                        createdAt = currentTime - 4L
                    ),
                    Message.Text(
                        id = 3L,
                        chatRoomId = 1L,
                        userId = 1L,
                        content = "해당 물품 구매 희망합니다.",
                        createdAt = currentTime - 3L
                    ),
                    Message.Text(
                        id = 4L,
                        chatRoomId = 1L,
                        userId = 2L,
                        content = "이거 말씀하시는 건가요?",
                        createdAt = currentTime - 2L
                    ),
                    Message.Image(
                        id = 5L,
                        chatRoomId = 1L,
                        userId = 2L,
                        content = "https://placehold.co/600x400",
                        createdAt = currentTime - 1L
                    ),
                    Message.Text(
                        id = 6L,
                        chatRoomId = 1L,
                        userId = 2L,
                        content = "엄청나게 긴 메세지를 엄청나게 보내는데 ".repeat(20),
                        createdAt = currentTime
                    ),
                ),
            )
        }
    }

    override suspend fun readMessage(
        id: Long
    ): Result<Unit> {
        randomShortDelay()

        return Result.success(Unit)
    }

    override suspend fun connectRoom(): Result<Session> {
        randomShortDelay()

        return Result.success(
            Session(
                subscribe = { flow { } },
                send = { _, _, _ -> Result.success(Unit) },
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
