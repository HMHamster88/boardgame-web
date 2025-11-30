package com.hmhamster.boardgame.db.entities

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "games")
class GameEntity(
    var id: String? = null,
    @Indexed(unique = true)
    var name: String,
    var type: GameType,
    var status: GameStatus,
    var minPlayers: Int = 2,
    var maxPlayers: Int,
    @DBRef
    var owner: UserEntity,
    var players: MutableList<PlayerEntity> = mutableListOf(),
    var settings: GameSettingsEntity,
    @CreatedDate
    val createdDate: LocalDateTime? = null,
    @LastModifiedDate
    val lastModifiedDate: LocalDateTime? = null
) {
    fun getPlayerByUserId(userId: String): PlayerEntity? {
        return players.find { it -> it.user.id == userId }
    }
}