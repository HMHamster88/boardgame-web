package com.hmhamster.boardgame.dto.websocket

import com.hmhamster.boardgame.websocket.GameConnection
import com.hmhamster.boardgame.websocket.GameMessageHandler
import io.swagger.v3.oas.annotations.media.Schema

@Schema
class KickPlayerRequest(
    var userId: String
) : GameWebsocketMessage {
    override fun handle(messageHandler: GameMessageHandler, connection: GameConnection) {
        messageHandler.handleKickPlayer(connection, this)
    }
}