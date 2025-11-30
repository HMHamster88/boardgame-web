package com.hmhamster.boardgame.dto.websocket

import com.hmhamster.boardgame.dto.game.Game
import com.hmhamster.boardgame.dto.game.GameState
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "For WS client")
class GameStartedMessage(
    var game: Game,
    var gameState: GameState?
) : GameWebsocketMessage

