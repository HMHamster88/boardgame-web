package com.hmhamster.boardgame.dto.auth

data class SignUpResponse (
    val accessToken: String,
    val refreshToken: String,
    val user: User
)