package com.hmhamster.boardgame.config

import com.hmhamster.boardgame.dto.game.GameSettings
import com.hmhamster.boardgame.dto.game.GameState
import com.hmhamster.boardgame.dto.websocket.GameStateChange
import com.hmhamster.boardgame.dto.websocket.GameWebsocketMessage
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.core.converter.ResolvedSchema
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.models.OpenAPI
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApiConfig {

    val classesToScan = listOf(
        GameWebsocketMessage::class,
        GameSettings::class,
        GameState::class,
        GameStateChange::class
    )

    @Bean
    fun addAllClassesFormPackToSwagger(): OpenApiCustomizer? {
        return OpenApiCustomizer { openApi ->
            val reflections = Reflections("com.hmhamster.boardgame", SubTypesScanner(false))
            for (cls in classesToScan) {
                addSchema(cls.java, openApi)
                val subtypes = reflections.getSubTypesOf(cls.java)
                for (schemaClass in subtypes) {
                    addSchema(schemaClass, openApi)
                }
            }
        }
    }

    fun addSchema(schemaClass: Class<out Any?>, openApi: OpenAPI) {
        val name = getName(schemaClass)
        if (!openApi.components.schemas.containsKey(name)) {
            val resolvedSchema: ResolvedSchema = ModelConverters.getInstance()
                .readAllAsResolvedSchema(schemaClass)
            if (resolvedSchema.schema.name != null) openApi.schema(
                resolvedSchema.schema.name,
                resolvedSchema.schema
            )
        }
    }

    fun getName(schemaClass: Class<out Any?>): String {
        val schema: Schema? = schemaClass.getAnnotation(Schema::class.java)
        if (schema == null || schema.name.isEmpty()) {
            return schemaClass.getSimpleName()
        }
        return schema.name
    }
}