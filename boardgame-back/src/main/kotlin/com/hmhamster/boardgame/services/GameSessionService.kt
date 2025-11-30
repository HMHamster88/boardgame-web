package com.hmhamster.boardgame.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.hmhamster.boardgame.db.entities.GameStateEntity
import com.hmhamster.boardgame.db.repositories.GameRepository
import com.hmhamster.boardgame.db.repositories.GameStateRepository
import com.hmhamster.boardgame.dto.websocket.GameWebsocketMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

@Service
class GameSessionService(
    val gameRepository: GameRepository,
    val gameStateRepository: GameStateRepository,
    val objectMapper: ObjectMapper
) {
    private val logger = KotlinLogging.logger {}

    val sessionByGameId: MutableMap<String, GameSession> = mutableMapOf()

    fun createGameSession(gameId: String): GameSession {
        val gameEntity = gameRepository.findById(gameId).orElseThrow()
        val gameState: GameStateEntity? = gameStateRepository.findByGameId(gameId).orElse(null)
        var session = sessionByGameId[gameId]
        if (session == null) {
            session = GameSession(gameId, gameState?.id)
            sessionByGameId.put(gameId, session)
        }
        logger.info { "Game $gameId loaded" }
        return session
    }

    fun closeSession(gameId: String) {
        var session = sessionByGameId[gameId]
        if (session == null) {
            throw NoSuchElementException("No game with id: $gameId")
        }
        sessionByGameId.remove(gameId)
        logger.info { "Game unloaded $gameId loaded" }
    }

    fun sendToAll(session: GameSession, message: GameWebsocketMessage) {
        val json = objectMapper.writeValueAsString(message)
        session.gameConnections.forEach { connection ->
            try {
                connection.session.sendMessage(TextMessage(json))
            } catch (ex: Exception) {
                logger.error(ex) { "Failed send message ${connection.session.id}" }
            }
        }
    }
}