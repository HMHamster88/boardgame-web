package com.hmhamster.boardgame.dto.websocket

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.hmhamster.boardgame.websocket.GameConnection
import com.hmhamster.boardgame.websocket.GameMessageHandler
import io.swagger.v3.oas.annotations.media.Schema

@Schema
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@type",
    visible = false
)
interface GameWebsocketMessage {
    fun handle(messageHandler: GameMessageHandler, connection: GameConnection) {}
}