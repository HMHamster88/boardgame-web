package com.hmhamster.boardgame.games.tictactoe.dto

import com.hmhamster.boardgame.dto.game.GameSettings
import io.swagger.v3.oas.annotations.media.Schema

@Schema
class TicTacToeGameSettings(
    var fieldSize: Int
) : GameSettings()