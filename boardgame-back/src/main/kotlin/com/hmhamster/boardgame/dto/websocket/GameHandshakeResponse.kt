package com.hmhamster.boardgame.dto.websocket

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "For WS client")
class GameHandshakeResponse(
    val success: Boolean
) : GameWebsocketMessage