package com.hmhamster.boardgame.dto.auth

data class SignInResponse (
    val accessToken: String,
    val refreshToken: String,
    val user: User
)