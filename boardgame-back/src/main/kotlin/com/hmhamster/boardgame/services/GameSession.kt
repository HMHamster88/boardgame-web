package com.hmhamster.boardgame.services

import com.hmhamster.boardgame.websocket.GameConnection

class GameSession(
    val gameId: String,
    var gameStateId: String?,
    val gameConnections: MutableList<GameConnection> = mutableListOf()
)