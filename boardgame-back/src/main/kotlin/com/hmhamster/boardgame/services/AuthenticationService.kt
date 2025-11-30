package com.hmhamster.boardgame.services

import com.hmhamster.boardgame.converters.GeneralConverter
import com.hmhamster.boardgame.db.entities.Role
import com.hmhamster.boardgame.db.entities.UserEntity
import com.hmhamster.boardgame.db.repositories.UserRepository
import com.hmhamster.boardgame.dto.auth.SignInRequest
import com.hmhamster.boardgame.dto.auth.SignInResponse
import com.hmhamster.boardgame.dto.auth.SignUpRequest
import com.hmhamster.boardgame.dto.auth.RefreshTokenResponse
import com.hmhamster.boardgame.dto.auth.SignUpResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Date

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val generalConverter: GeneralConverter,
    @Value("\${jwt.accessTokenExpiration}") private val accessTokenExpiration: Long = 0,
    @Value("\${jwt.refreshTokenExpiration}") private val refreshTokenExpiration: Long = 0
) {
    fun signIn(signInRequest: SignInRequest): SignInResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                signInRequest.username,
                signInRequest.password
            )
        )

        val user = userDetailsService.loadUserByUsername(signInRequest.username)

        val accessToken = createAccessToken(user)
        val refreshToken = createRefreshToken(user)

        refreshTokenRepository.save(refreshToken, user)

        val userEntity = userRepository.findByUsername(signInRequest.username)
        if (userEntity == null) {
            throw AuthenticationServiceException("User ${user.username} not exists")
        }

        return SignInResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            user = generalConverter.toDto(userEntity)
        )
    }

    fun refreshAccessToken(refreshToken: String): RefreshTokenResponse {
        val username = tokenService.extractUsername(refreshToken)

        return username.let { user ->
            val currentUserDetails = userDetailsService.loadUserByUsername(user)
            val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(refreshToken)

            if (currentUserDetails.username == refreshTokenUserDetails?.username) {

                val newAccessToken = createAccessToken(currentUserDetails)
                val newRefreshToken = createRefreshToken(currentUserDetails)

                refreshTokenRepository.save(newRefreshToken, currentUserDetails)

                return RefreshTokenResponse(
                    accessToken = newAccessToken,
                    refreshToken = newRefreshToken
                )
            }
            else
                throw AuthenticationServiceException("Invalid refresh token")
        }
    }

    private fun createAccessToken(user: UserDetails): String {
        return tokenService.generateToken(
            subject = user.username,
            expiration = Date(System.currentTimeMillis() + accessTokenExpiration)
        )
    }

    private fun createRefreshToken(user: UserDetails) = tokenService.generateToken(
        subject = user.username,
        expiration = Date(System.currentTimeMillis() + refreshTokenExpiration)
    )

    fun signUp(request: SignUpRequest): SignUpResponse {
        val user = userRepository.findByUsername(request.username)
        if (user != null) {
            throw AuthenticationServiceException("User ${user.username} already exists")
        }
        val newUser = UserEntity(null, request.username, passwordEncoder.encode(request.password), role = Role.USER)
        userRepository.save<UserEntity>(newUser)
        val userDetails = userDetailsService.loadUserByUsername(request.username)

        val accessToken = createAccessToken(userDetails)
        val refreshToken = createRefreshToken(userDetails)

        refreshTokenRepository.save(refreshToken, userDetails)

        return SignUpResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            user = generalConverter.toDto(newUser)
        )
    }
}