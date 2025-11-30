package com.hmhamster.boardgame.controllers

import com.hmhamster.boardgame.dto.auth.SignInRequest
import com.hmhamster.boardgame.dto.auth.SignInResponse
import com.hmhamster.boardgame.dto.auth.RefreshTokenRequest
import com.hmhamster.boardgame.dto.auth.SignUpRequest
import com.hmhamster.boardgame.dto.auth.RefreshTokenResponse
import com.hmhamster.boardgame.dto.auth.SignUpResponse
import com.hmhamster.boardgame.services.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/signin")
    fun signIn(
        @RequestBody authRequest: SignInRequest
    ): SignInResponse =
        authenticationService.signIn(authRequest)


    @PostMapping("/signup")
    fun signUp(
        @RequestBody signUpRequest: SignUpRequest
    ): SignUpResponse =
        authenticationService.signUp(signUpRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest
    ): RefreshTokenResponse = authenticationService.refreshAccessToken(request.refreshToken)
}