package com.hmhamster.boardgame.db.repositories

import com.hmhamster.boardgame.db.entities.GameStateEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GameStateRepository : MongoRepository<GameStateEntity, String> {
    fun findByGameId(gameId: String): Optional<GameStateEntity>
}