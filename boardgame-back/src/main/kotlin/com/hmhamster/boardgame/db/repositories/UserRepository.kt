package com.hmhamster.boardgame.db.repositories

import com.hmhamster.boardgame.db.entities.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<UserEntity, String> {
    fun findByUsername(username: String?): UserEntity?
}