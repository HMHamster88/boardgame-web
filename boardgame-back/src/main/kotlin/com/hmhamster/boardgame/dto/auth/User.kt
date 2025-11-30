package com.hmhamster.boardgame.dto.auth

import com.hmhamster.boardgame.db.entities.Role

data class User(
    var id: String? = null,
    var username: String,
    var role: Role
)