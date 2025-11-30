package com.hmhamster.boardgame.dto.game

import com.hmhamster.boardgame.db.entities.GameStatus
import com.hmhamster.boardgame.db.entities.GameType
import com.hmhamster.boardgame.dto.auth.User

data class Game(
    var id: String? = null,
    var name: String,
    var type: GameType,
    var minPlayers: Int,
    var maxPlayers: Int,
    var owner: User,
    var players: List<Player> = listOf(),
    var status: GameStatus,
    var settings: GameSettings
)