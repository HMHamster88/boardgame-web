package com.hmhamster.boardgame.converters

import com.hmhamster.boardgame.config.MappingConfiguration
import com.hmhamster.boardgame.db.entities.*
import com.hmhamster.boardgame.dto.auth.User
import com.hmhamster.boardgame.dto.game.Game
import com.hmhamster.boardgame.dto.game.GameSettings
import com.hmhamster.boardgame.dto.game.GameState
import com.hmhamster.boardgame.dto.game.Player
import com.hmhamster.boardgame.games.tictactoe.dto.TicTacToeGameSettings
import com.hmhamster.boardgame.games.tictactoe.dto.TicTacToeGameState
import com.hmhamster.boardgame.games.tictactoe.entities.TicTacToeGameSettingsEntity
import com.hmhamster.boardgame.games.tictactoe.entities.TicTacToeGameStateEntity
import org.mapstruct.Mapper
import org.mapstruct.SubclassMapping

@Mapper(config = MappingConfiguration::class)
interface GeneralConverter {

    fun toDto(entity: GameEntity): Game
    fun toDto(entity: List<GameEntity>): List<Game>

    fun toDto(user: UserEntity): User

    fun toDto(playerEntity: PlayerEntity): Player

    @SubclassMapping(source = TicTacToeGameSettingsEntity::class, target = TicTacToeGameSettings::class)
    fun toDto(settingsEntity: GameSettingsEntity): GameSettings

    @SubclassMapping(source = TicTacToeGameSettings::class, target = TicTacToeGameSettingsEntity::class)
    fun toEntity(settings: GameSettings): GameSettingsEntity

    @SubclassMapping(source = TicTacToeGameStateEntity::class, target = TicTacToeGameState::class)
    fun toDto(gameStateEntity: GameStateEntity?): GameState?
}