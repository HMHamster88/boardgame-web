package com.hmhamster.boardgame.games.tictactoe.entities

import com.hmhamster.boardgame.db.entities.GameSettingsEntity

class TicTacToeGameSettingsEntity(
    var fieldSize: Int = 3
) : GameSettingsEntity()