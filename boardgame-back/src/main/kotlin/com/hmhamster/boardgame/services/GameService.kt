package com.hmhamster.boardgame.services

import com.hmhamster.boardgame.converters.GeneralConverter
import com.hmhamster.boardgame.db.entities.*
import com.hmhamster.boardgame.db.repositories.GameRepository
import com.hmhamster.boardgame.db.repositories.GameStateRepository
import com.hmhamster.boardgame.dto.game.CreateGameRequest
import com.hmhamster.boardgame.dto.game.CreateGameResponse
import com.hmhamster.boardgame.dto.game.GameListResponse
import com.hmhamster.boardgame.dto.game.GameResponse
import com.hmhamster.boardgame.games.tictactoe.entities.TicTacToeGameSettingsEntity
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class GameService(
    val gameRepository: GameRepository,
    val gameStateRepository: GameStateRepository,
    val generalConverter: GeneralConverter
) {


    fun getGames(): GameListResponse {
        val allGames = gameRepository.findAll()
        return GameListResponse(generalConverter.toDto(allGames))
    }

    fun createGame(request: CreateGameRequest): CreateGameResponse {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        val newGame =
            gameRepository.save<GameEntity>(
                GameEntity(
                    null, request.name, request.type, GameStatus.CREATED, 2, 2, user,
                    settings = createGameSettings(request.type)
                )
            )
        return CreateGameResponse(generalConverter.toDto(newGame))
    }

    fun createGameSettings(gameType: GameType): GameSettingsEntity {
        return TicTacToeGameSettingsEntity()
    }

    fun getGame(gameId: String): GameResponse {
        val game = gameRepository.findById(gameId).orElseThrow()
        val gameState = gameStateRepository.findByGameId(gameId).orElse(null)
        return GameResponse(generalConverter.toDto(game), generalConverter.toDto(gameState))
    }

    fun deleteGame(id: String) {
        val game = gameRepository.findById(id).orElseThrow()
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        if (game.owner.id != user.id) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You are not owner of this game")
        }
        gameRepository.delete(game)
    }
}