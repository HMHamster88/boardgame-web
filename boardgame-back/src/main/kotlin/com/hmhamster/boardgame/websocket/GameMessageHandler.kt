package com.hmhamster.boardgame.websocket

import com.hmhamster.boardgame.dto.websocket.*

interface GameMessageHandler {
    fun handleHandshake(connection: GameConnection, message: GameHandshakeRequest)
    fun handleJoin(connection: GameConnection, message: JoinGameRequest)
    fun handleUpdatePlayer(connection: GameConnection, message: UpdatePlayerMessage)
    fun handleKickPlayer(connection: GameConnection, request: KickPlayerRequest)
    fun handleStartGame(connection: GameConnection, request: StartGameRequest)
    fun handleGameAction(connection: GameConnection, message: GameActionMessage)
}