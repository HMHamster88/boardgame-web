package com.hmhamster.boardgame.dto.game

open class GameState(
    var id: String? = null,
    val gameId: String,
    val playersStates: MutableList<PlayerState> = mutableListOf(),
    var activePlayerIndex: Int = 0
)