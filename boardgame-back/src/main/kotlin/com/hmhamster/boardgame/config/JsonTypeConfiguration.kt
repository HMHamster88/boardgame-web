package com.hmhamster.boardgame.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.NamedType
import com.hmhamster.boardgame.dto.game.GameSettings
import com.hmhamster.boardgame.dto.game.GameState
import com.hmhamster.boardgame.dto.websocket.GameStateChange
import com.hmhamster.boardgame.dto.websocket.GameWebsocketMessage
import jakarta.annotation.PostConstruct
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.springframework.context.annotation.Configuration

@Configuration
class JsonTypeConfiguration(
    private val objectMapper: ObjectMapper
) {

    val classesToScan = listOf(
        GameWebsocketMessage::class,
        GameSettings::class,
        GameState::class,
        GameStateChange::class
    )

    @PostConstruct
    fun registerSubtypes() {
        val reflections = Reflections("com.hmhamster.boardgame", SubTypesScanner(false))
        for (cls in classesToScan) {
            val subtypes = reflections.getSubTypesOf(cls.java)
            for (subclass in subtypes) {
                objectMapper.registerSubtypes(
                    NamedType(subclass, subclass.simpleName)
                )
            }
        }
    }
}