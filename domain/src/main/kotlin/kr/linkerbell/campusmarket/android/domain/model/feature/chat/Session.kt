package kr.linkerbell.campusmarket.android.domain.model.feature.chat

import kotlinx.coroutines.flow.Flow

class Session(
    val subscribe: suspend () -> Flow<Message>,
    val send: suspend (Message) -> Unit,
    val disconnect: suspend () -> Unit,
)
