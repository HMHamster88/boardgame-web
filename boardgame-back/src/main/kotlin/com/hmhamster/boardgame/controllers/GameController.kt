package com.hmhamster.boardgame.controllers

import com.hmhamster.boardgame.dto.game.CreateGameRequest
import com.hmhamster.boardgame.dto.game.CreateGameResponse
import com.hmhamster.boardgame.dto.game.GameListResponse
import com.hmhamster.boardgame.dto.game.GameResponse
import com.hmhamster.boardgame.services.GameService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/games")
class GameController(
    val gameService: GameService
) {
    @GetMapping
    fun games(): GameListResponse {
        return gameService.getGames()
    }

    @GetMapping("/{id}")
    fun game(
        @PathVariable
        id: String
    ): GameResponse {
        return gameService.getGame(id)
    }

    @PostMapping
    fun createGame(@RequestBody createGameRequest: CreateGameRequest): CreateGameResponse {
        return gameService.createGame(createGameRequest)
    }

    @DeleteMapping("/{id}")
    fun deleteGame(
        @PathVariable
        id: String
    ) {
        gameService.deleteGame(id)
    }
}