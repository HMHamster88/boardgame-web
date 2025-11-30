package com.hmhamster.boardgame.db.entities

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "gameState")
open class GameStateEntity(
    var id: String? = null,
    val gameId: String,
    val playersStates: MutableList<PlayerStateEntity> = mutableListOf(),
    var activePlayerIndex: Int = 0
)