package com.hmhamster.boardgame.dto.game

import com.hmhamster.boardgame.db.entities.GameType

data class CreateGameRequest(
    val name: String,
    val type: GameType
)
