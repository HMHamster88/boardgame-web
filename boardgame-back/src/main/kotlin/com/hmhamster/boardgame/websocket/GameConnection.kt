package com.hmhamster.boardgame.websocket

import com.hmhamster.boardgame.db.entities.UserEntity
import org.springframework.web.socket.WebSocketSession

class GameConnection(
    val session: WebSocketSession,
    var user: UserEntity? = null,
    var gameId: String? = null
)