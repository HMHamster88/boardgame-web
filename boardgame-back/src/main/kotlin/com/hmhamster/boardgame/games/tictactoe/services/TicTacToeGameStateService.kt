package com.hmhamster.boardgame.games.tictactoe.services

import com.hmhamster.boardgame.converters.GeneralConverter
import com.hmhamster.boardgame.db.entities.GameEntity
import com.hmhamster.boardgame.db.entities.GameStateEntity
import com.hmhamster.boardgame.db.entities.GameType
import com.hmhamster.boardgame.db.entities.PlayerEntity
import com.hmhamster.boardgame.dto.websocket.GameActionMessage
import com.hmhamster.boardgame.dto.websocket.GameFinishedMessage
import com.hmhamster.boardgame.dto.websocket.GameStateChange
import com.hmhamster.boardgame.games.tictactoe.dto.TicTacToeCellChanged
import com.hmhamster.boardgame.games.tictactoe.dto.TicTacToeSetCellAction
import com.hmhamster.boardgame.games.tictactoe.entities.TicTacToeFieldValue
import com.hmhamster.boardgame.games.tictactoe.entities.TicTacToeGameSettingsEntity
import com.hmhamster.boardgame.games.tictactoe.entities.TicTacToeGameStateEntity
import com.hmhamster.boardgame.services.GameStateService
import com.hmhamster.boardgame.services.GameStateServiceType
import org.springframework.stereotype.Component

@GameStateServiceType(GameType.TIC_TAC_TOE)
@Component
class TicTacToeGameStateService(
    private val converter: GeneralConverter
) : GameStateService {
    override fun createGameState(gameEntity: GameEntity): GameStateEntity {
        val settings = gameEntity.settings as TicTacToeGameSettingsEntity
        var field = MutableList(settings.fieldSize) { MutableList(settings.fieldSize) { TicTacToeFieldValue.NONE } }
        return TicTacToeGameStateEntity(gameId = gameEntity.id!!, field = field)
    }

    override fun performAction(
        gameEntity: GameEntity,
        gameState: GameStateEntity,
        playerEntity: PlayerEntity,
        message: GameActionMessage
    ): GameStateChange {
        val action = message as TicTacToeSetCellAction
        val state = gameState as TicTacToeGameStateEntity
        if (state.field[action.y][action.x] != TicTacToeFieldValue.NONE) {
            throw Error("Cell (${action.x}, ${action.y}) already set")
        }
        val playerIndex = gameEntity.players.indexOfFirst { it -> it.user.id == playerEntity.user.id }
        if (state.activePlayerIndex != playerIndex) {
            throw Error("You are not active player")
        }
        val newValue = when (playerIndex) {
            0 -> TicTacToeFieldValue.CROSS
            else -> TicTacToeFieldValue.ZERO
        }
        state.field[action.y][action.x] = newValue
        state.activePlayerIndex = (state.activePlayerIndex + 1) % gameEntity.players.size
        return TicTacToeCellChanged(action.x, action.y, newValue, state.activePlayerIndex)
    }

    override fun checkFinished(
        gameEntity: GameEntity,
        gameState: GameStateEntity
    ): GameFinishedMessage? {
        val settings = gameEntity.settings as TicTacToeGameSettingsEntity
        var state = gameState as TicTacToeGameStateEntity
        var fieldSize = settings.fieldSize
        val range = 0.until(fieldSize)
        val linesCoords = (range.map { x -> range.map { y -> Pair(y, x) } } + // horizontal lines
                range.map { x -> range.map { y -> Pair(x, y) } } + // vertical lines
                listOf(range.map { x -> Pair(x, x) }) + // main diagonal
                listOf(range.map { x -> Pair(fieldSize - x - 1, x) }))
        var lines = linesCoords // second diagonal
            .map { line -> line.map { coords -> gameState.field[coords.first][coords.second] } }
        val winner = lines
            .map { line ->
                line.reduce { a, b ->
                    when (a == b) {
                        true -> a
                        else -> TicTacToeFieldValue.NONE
                    }
                }
            }
            .reduce { a, b ->
                when {
                    a != TicTacToeFieldValue.NONE -> a
                    b != TicTacToeFieldValue.NONE -> b
                    else -> TicTacToeFieldValue.NONE
                }
            }
        if (winner != TicTacToeFieldValue.NONE) {
            return GameFinishedMessage(
                null, listOf(
                    converter.toDto(
                        when (winner) {
                            TicTacToeFieldValue.CROSS -> gameEntity.players[0]
                            TicTacToeFieldValue.ZERO -> gameEntity.players[1]
                            else -> throw Error()
                        }
                    )
                )
            )
        }
        val atLeastOneCellEmpty = state.field.flatMap { line -> line }.any { it == TicTacToeFieldValue.NONE }
        if (atLeastOneCellEmpty) {
            return null
        }
        return GameFinishedMessage(null, listOf())
    }
}