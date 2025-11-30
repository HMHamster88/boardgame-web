package com.hmhamster.boardgame.filters

import com.hmhamster.boardgame.services.TokenService
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerExceptionResolver


@Component
class JwtAuthorizationFilter(
    private val userDetailsService: UserDetailsService,
    private val tokenService: TokenService,
    @Qualifier("handlerExceptionResolver")
    private var resolver: HandlerExceptionResolver,
    @Value("\${server.unauthorizedUrls}")
    private val unauthorizedUrls: List<String>
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            if (shouldNotFilter(request)) {
                return
            }
            val authorizationHeader: String? = request.getHeader("Authorization")
            if (authorizationHeader == null) {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "No auth header")
                return
            }
            if (authorizationHeader.startsWith("Bearer ")) {
                val token: String = authorizationHeader.substringAfter("Bearer ")
                try {
                    val username: String = tokenService.extractUsername(token)

                    if (SecurityContextHolder.getContext().authentication == null) {
                        val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)

                        if (username == userDetails.username) {
                            val authToken = UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.authorities
                            )
                            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                            SecurityContextHolder.getContext().authentication = authToken
                        }
                    }
                    filterChain.doFilter(request, response)
                } catch (error: ExpiredJwtException) {
                    throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT expired")
                }


            }

        } catch (error: RuntimeException) {
            resolver.resolveException(request, response, null, error)
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return unauthorizedUrls.contains(request.requestURI.toString())
    }
}