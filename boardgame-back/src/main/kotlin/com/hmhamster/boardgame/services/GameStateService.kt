package com.hmhamster.boardgame.services

import com.hmhamster.boardgame.db.entities.GameEntity
import com.hmhamster.boardgame.db.entities.GameStateEntity
import com.hmhamster.boardgame.db.entities.GameType
import com.hmhamster.boardgame.db.entities.PlayerEntity
import com.hmhamster.boardgame.dto.websocket.GameActionMessage
import com.hmhamster.boardgame.dto.websocket.GameFinishedMessage
import com.hmhamster.boardgame.dto.websocket.GameStateChange
import org.springframework.stereotype.Service
import kotlin.reflect.full.findAnnotation


internal annotation class GameStateServiceType(val type: GameType)

interface GameStateService {
    fun createGameState(gameEntity: GameEntity): GameStateEntity
    fun performAction(
        gameEntity: GameEntity,
        gameState: GameStateEntity,
        playerEntity: PlayerEntity,
        message: GameActionMessage
    ): GameStateChange

    fun checkFinished(
        gameEntity: GameEntity,
        gameState: GameStateEntity
    ): GameFinishedMessage?
}

@Service
class GameStateServices(services: MutableCollection<GameStateService>) {
    private var gameStatesServices: MutableMap<GameType, GameStateService> = mutableMapOf()

    init {
        for (service in services) {
            val stageType: GameStateServiceType? = service::class.findAnnotation()
            stageType?.let { gameStatesServices[it.type] = service }
        }
    }

    fun getService(type: GameType): GameStateService {
        return gameStatesServices[type]!!
    }
}