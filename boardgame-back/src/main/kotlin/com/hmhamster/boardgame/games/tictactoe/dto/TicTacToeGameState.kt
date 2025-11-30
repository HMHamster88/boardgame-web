package com.hmhamster.boardgame.games.tictactoe.dto

import com.hmhamster.boardgame.dto.game.GameState
import com.hmhamster.boardgame.games.tictactoe.entities.TicTacToeFieldValue
import io.swagger.v3.oas.annotations.media.Schema

@Schema
class TicTacToeGameState(
    gameId: String,
    val field: List<List<TicTacToeFieldValue>>
) : GameState(gameId = gameId)