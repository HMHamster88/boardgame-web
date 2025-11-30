package com.hmhamster.boardgame.games.tictactoe.dto

import com.hmhamster.boardgame.dto.websocket.GameActionMessage
import io.swagger.v3.oas.annotations.media.Schema

@Schema
class TicTacToeSetCellAction(
    val x: Int,
    val y: Int
) : GameActionMessage()