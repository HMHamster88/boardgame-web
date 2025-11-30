package com.hmhamster.boardgame.db.repositories

import com.hmhamster.boardgame.db.entities.GameEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : MongoRepository<GameEntity, String>