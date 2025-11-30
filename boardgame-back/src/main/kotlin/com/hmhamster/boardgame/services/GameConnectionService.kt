package com.hmhamster.boardgame.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.hmhamster.boardgame.converters.GeneralConverter
import com.hmhamster.boardgame.db.entities.GameStatus
import com.hmhamster.boardgame.db.entities.PlayerEntity
import com.hmhamster.boardgame.db.repositories.GameRepository
import com.hmhamster.boardgame.db.repositories.GameStateRepository
import com.hmhamster.boardgame.db.repositories.UserRepository
import com.hmhamster.boardgame.dto.websocket.*
import com.hmhamster.boardgame.websocket.GameConnection
import com.hmhamster.boardgame.websocket.GameMessageHandler
import io.github.oshai.kotlinlogging.KotlinLogging
import javassist.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class GameConnectionService(
    private val tokenService: TokenService,
    private val userRepository: UserRepository,
    private val gameRepository: GameRepository,
    private val gameStateRepository: GameStateRepository,
    private val objectMapper: ObjectMapper,
    private val gameSessionService: GameSessionService,
    private val gameStateServices: GameStateServices,
    private val converter: GeneralConverter
) : GameMessageHandler {
    private val logger = KotlinLogging.logger {}
    val connectionsBySessionId: MutableMap<String, GameConnection> = mutableMapOf()

    fun createConnection(session: WebSocketSession): GameConnection {
        val connection = GameConnection(session)
        connectionsBySessionId.put(session.id, connection)
        logger.info { "Connection created ${session.id}" }
        return connection
    }

    fun removeConnection(sessionId: String) {
        if (!connectionsBySessionId.contains(sessionId)) {
            logger.info { "No connection with id $sessionId" }
            return
        }
        val connection = connectionsBySessionId.getValue(sessionId)
        val gameId = connection.gameId!!
        val session = gameSessionService.createGameSession(gameId)
        connectionsBySessionId.remove(sessionId)
        session.gameConnections.remove(connection)
        if (session.gameConnections.isEmpty()) {
            gameSessionService.closeSession(gameId)
        } else {
            val game = gameRepository.findById(gameId).orElseThrow()
            val player = game.getPlayerByUserId(connection.user?.id!!)
            if (player != null) {
                player.online = false
                gameSessionService.sendToAll(session, PlayerChangedMessage(converter.toDto(player)))
                gameRepository.save(game)
            }
        }
        logger.info { "Connection removed $sessionId" }
    }

    fun handleMessage(sessionId: String, message: GameWebsocketMessage) {
        val connection = connectionsBySessionId[sessionId]
        if (connection == null) {
            return
        }
        message.handle(this, connection)
    }

    fun sendMessage(connection: GameConnection, message: GameWebsocketMessage) {
        connection.session.sendMessage(TextMessage(objectMapper.writeValueAsString(message)))
    }

    fun sendError(connection: GameConnection, message: String) {
        sendMessage(connection, GameErrorMessage(message))
    }

    override fun handleHandshake(
        connection: GameConnection,
        message: GameHandshakeRequest
    ) {
        try {
            connection.gameId = message.gameId
            val userName = tokenService.extractUsername(message.accessToken)
            val user = userRepository.findByUsername(userName)
            if (user == null) {
                throw NotFoundException("User not found for name: $userName")
            }
            connection.user = user
            val gameSession = gameSessionService.createGameSession(connection.gameId!!)
            gameSession.gameConnections.add(connection)
            logger.info { "Handshake user: ${userName}, gameId: ${message.gameId}" }
            sendMessage(connection, GameHandshakeResponse(true))
        } catch (error: Exception) {
            logger.error(error) { "Failed to handshake" }
            sendMessage(connection, GameHandshakeResponse(false))
        }
    }

    override fun handleJoin(
        connection: GameConnection,
        message: JoinGameRequest
    ) {
        try {
            val gameSession = gameSessionService.createGameSession(connection.gameId!!)
            val game = gameRepository.findById(gameSession.gameId).orElseThrow()
            var player = game.getPlayerByUserId(connection.user?.id!!)

            if (player == null) {
                if (game.players.size >= game.maxPlayers) {
                    throw Exception("Maximum players exceed")
                }
                val user = connection.user!!

                player = PlayerEntity(user, user.username!!, "#00ff37")
                player.user = connection.user!!
                game.players.add(player)
            }
            player.online = true
            gameRepository.save(game)
            sendMessage(connection, JoinGameResponse(true))
            gameSessionService.sendToAll(gameSession, PlayerChangedMessage(converter.toDto(player)))
        } catch (ex: Exception) {
            logger.error(ex) { "Join game error" }
            sendMessage(connection, JoinGameResponse(false))
        }
    }

    override fun handleUpdatePlayer(
        connection: GameConnection,
        message: UpdatePlayerMessage
    ) {
        val gameSession = gameSessionService.createGameSession(connection.gameId!!)
        val game = gameRepository.findById(gameSession.gameId).orElseThrow()
        var player = game.getPlayerByUserId(connection.user?.id!!)
        if (player == null) {
            throw NotFoundException("Player not found")
        }
        player.name = message.name
        player.color = message.color
        gameRepository.save(game)
        gameSessionService.sendToAll(gameSession, PlayerChangedMessage(converter.toDto(player)))
    }

    override fun handleKickPlayer(
        connection: GameConnection,
        request: KickPlayerRequest
    ) {
        val gameSession = gameSessionService.createGameSession(connection.gameId!!)
        val game = gameRepository.findById(gameSession.gameId).orElseThrow()
        val player = game.players.find { it -> it.user.id == request.userId }
        if (player == null) {
            throw NotFoundException("Player not found ${request.userId}")
        }
        if (game.owner.id != connection.user?.id && player.user.id != connection.user?.id) {
            sendError(connection, "Cant kick player")
            return
        }
        game.players.remove(player)
        gameRepository.save(game)
        gameSessionService.sendToAll(gameSession, PlayerKickedMessage(request.userId))
    }

    override fun handleStartGame(
        connection: GameConnection,
        request: StartGameRequest
    ) {
        val gameSession = gameSessionService.createGameSession(connection.gameId!!)
        val game = gameRepository.findById(gameSession.gameId).orElseThrow()
        game.settings = converter.toEntity(request.gameSettings)
        val gameStateService = gameStateServices.getService(game.type)
        val state = gameStateService.createGameState(game)
        gameStateRepository.save(state)
        gameSession.gameStateId = state.id
        game.status = GameStatus.STARTED
        gameRepository.save(game)
        gameSessionService.sendToAll(gameSession, GameStartedMessage(converter.toDto(game), converter.toDto(state)))
    }

    override fun handleGameAction(
        connection: GameConnection,
        message: GameActionMessage
    ) {
        try {
            val gameSession = gameSessionService.createGameSession(connection.gameId!!)
            val game = gameRepository.findById(gameSession.gameId).orElseThrow()
            val gameStateService = gameStateServices.getService(game.type)
            val player = game.players.find { it -> it.user.id == connection.user?.id }
            val state = gameStateRepository.findById(gameSession.gameStateId!!).orElseThrow()
            var stateChange = gameStateService.performAction(game, state, player!!, message)
            gameStateRepository.save(state)
            gameSessionService.sendToAll(gameSession, GameStateChangedMessage(stateChange))
            var gameFinishedMessage = gameStateService.checkFinished(game, state)
            if (gameFinishedMessage != null) {
                game.status = GameStatus.FINISHED
                gameSessionService.sendToAll(gameSession, gameFinishedMessage)
                gameRepository.save(game)
            }
        } catch (ex: Throwable) {
            sendMessage(connection, GameErrorMessage(ex.message ?: "Action error"))
            logger.error(ex) { "Perform action error" }
        }
    }
}