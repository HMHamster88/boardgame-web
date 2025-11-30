package com.hmhamster.boardgame.dto.websocket

import com.hmhamster.boardgame.dto.game.GameSettings
import com.hmhamster.boardgame.websocket.GameConnection
import com.hmhamster.boardgame.websocket.GameMessageHandler
import io.swagger.v3.oas.annotations.media.Schema

@Schema
class StartGameRequest(
    var gameSettings: GameSettings
) : GameWebsocketMessage {
    override fun handle(messageHandler: GameMessageHandler, connection: GameConnection) {
        messageHandler.handleStartGame(connection, this)
    }
}