package com.hmhamster.boardgame.db.entities

import org.springframework.data.mongodb.core.mapping.DBRef

class PlayerEntity(
    @DBRef
    var user: UserEntity,
    var name: String,
    var color: String,

    ) {
    var online: Boolean = false
}