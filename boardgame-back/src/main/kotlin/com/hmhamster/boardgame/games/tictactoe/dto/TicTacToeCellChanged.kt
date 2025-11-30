package com.hmhamster.boardgame.games.tictactoe.dto

import com.hmhamster.boardgame.dto.websocket.GameStateChange
import com.hmhamster.boardgame.games.tictactoe.entities.TicTacToeFieldValue
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode

@Schema
class TicTacToeCellChanged(
    @field:Schema(requiredMode = RequiredMode.REQUIRED)
    val x: Int,
    @field:Schema(requiredMode = RequiredMode.REQUIRED)
    val y: Int,
    @field:Schema(requiredMode = RequiredMode.REQUIRED)
    val value: TicTacToeFieldValue,
    @field:Schema(requiredMode = RequiredMode.REQUIRED)
    val activePlayerIndex: Int
) : GameStateChange()