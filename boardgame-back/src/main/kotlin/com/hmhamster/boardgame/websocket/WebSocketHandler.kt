package com.hmhamster.boardgame.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.hmhamster.boardgame.dto.websocket.GameWebsocketMessage
import com.hmhamster.boardgame.services.GameConnectionService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WebSocketHandler(
    val objectMapper: ObjectMapper,
    val gameConnectionService: GameConnectionService
) : TextWebSocketHandler() {
    private val logger = KotlinLogging.logger {}

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error(exception) { "Socket transport error" }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info { "Websocket connected: " + session.id }
        gameConnectionService.createConnection(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        try {
            println(message.payload)
            var wsMessage = objectMapper.readValue(
                message.payload as String,
                GameWebsocketMessage::class.java
            )

            logger.info { "Handle message: " + wsMessage.javaClass.name }
            gameConnectionService.handleMessage(session.id, wsMessage)
        } catch (error: Exception) {
            logger.error(error) { "Web socket handle message error" }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        gameConnectionService.removeConnection(session.id)
        logger.info { "Websocket closed: " + session.id }
    }
}