package com.hmhamster.boardgame.dto.game

import com.hmhamster.boardgame.dto.auth.User

class Player(
    val user: User,
    val name: String,
    val color: String,
    val online: Boolean
)