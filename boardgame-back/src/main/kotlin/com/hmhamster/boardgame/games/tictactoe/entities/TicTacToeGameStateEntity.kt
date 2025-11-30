package com.hmhamster.boardgame.games.tictactoe.entities

import com.hmhamster.boardgame.db.entities.GameStateEntity
import com.hmhamster.boardgame.db.entities.PlayerStateEntity

enum class TicTacToeFieldValue {
    NONE,
    CROSS,
    ZERO
}

class TicTacToeGameStateEntity(
    id: String? = null,
    gameId: String,
    playersStates: MutableList<PlayerStateEntity> = mutableListOf(),
    val field: MutableList<MutableList<TicTacToeFieldValue>>
) : GameStateEntity(
    id,
    gameId,
    playersStates
)