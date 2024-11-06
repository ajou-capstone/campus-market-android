package kr.linkerbell.campusmarket.android.domain.repository.feature

import kotlinx.coroutines.flow.Flow
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Message
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Room
import kr.linkerbell.campusmarket.android.domain.model.feature.chat.Session

interface ChatRepository {

    fun getRoomList(): Flow<List<Room>>

    fun getRoom(
        id: Long
    ): Flow<Room>

    suspend fun quitRoom(
        id: Long
    ): Result<Unit>

    suspend fun makeRoom(
        userId: Long,
        tradeId: Long
    ): Result<Room>

    suspend fun setRoomNotification(
        id: Long,
        isAlarm: Boolean
    ): Result<Unit>

    fun getMessageList(
        roomId: Long
    ): Flow<List<Message>>

    suspend fun readMessage(
        id: Long
    ): Result<Unit>

    suspend fun connectRoom(): Result<Session>
}
