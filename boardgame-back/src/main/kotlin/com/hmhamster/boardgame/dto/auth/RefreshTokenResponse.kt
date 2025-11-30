package com.hmhamster.boardgame.dto.auth

data class RefreshTokenResponse (
    val accessToken: String,
    val refreshToken: String
)
