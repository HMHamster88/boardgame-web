package com.hmhamster.boardgame.dto.websocket

import com.hmhamster.boardgame.dto.game.Player
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "For WS client")
class GameFinishedMessage(
    val message: String?,
    val winners: List<Player>
) : GameWebsocketMessage