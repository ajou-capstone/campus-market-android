package kr.linkerbell.campusmarket.android.domain.model.feature.chat

import kotlinx.coroutines.flow.Flow

class Session(
    val subscribe: suspend (id: Long) -> Flow<Message>,
    val send: suspend (id: Long, content: Any, contentType: String) -> Result<Unit>,
    val disconnect: suspend () -> Unit,
)
