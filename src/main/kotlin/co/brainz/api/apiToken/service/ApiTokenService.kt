/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.apiToken.service

import co.brainz.api.apiToken.entity.ApiTokenEntity
import co.brainz.api.apiToken.repository.ApiTokenRepository
import co.brainz.api.constants.ApiConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.user.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class ApiTokenService(
    private val userService: UserService,
    private val apiTokenRepository: ApiTokenRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Access Token 발행
     */
    fun createAccessToken(request: HttpServletRequest): ResponseEntity<*> {
        if (!this.paramsValid(request, ApiConstants.TokenType.ACCESS_TOKEN.code)) {
            throw AliceException(
                AliceErrorConstants.ERR_00001,
                AliceErrorConstants.ERR_00001.message + "[Parameter Error]"
            )
        }
        val userId = request.getHeader("userId") ?: ""
        val password = request.getHeader("password") ?: ""

        var status: HttpStatus? = null
        val userEntity = userService.selectUser(userId)
        val value = LinkedHashMap<String, Any>()
        val passwordEncoder = BCryptPasswordEncoder()
        when (passwordEncoder.matches(password, userEntity.password)) {
            true -> {
                var apiTokenEntity = ApiTokenEntity(
                    apiId = "",
                    accessToken = AliceUtil().getUUID(),
                    refreshToken = AliceUtil().getUUID(),
                    createDt = LocalDateTime.now(),
                    expiresIn = ApiConstants.API_EXPIRES_IN,
                    refreshTokenExpiresIn = ApiConstants.API_REFRESH_TOKEN_EXPIRES_IN,
                    requestUserKey = userEntity.userKey
                )
                apiTokenEntity = apiTokenRepository.save(apiTokenEntity)
                value["access_token"] = apiTokenEntity.accessToken
                value["expires_in"] = apiTokenEntity.expiresIn
                value["refresh_token"] = apiTokenEntity.refreshToken
                value["refresh_token_expires_in"] = apiTokenEntity.refreshTokenExpiresIn
                status = HttpStatus.OK
            }
            false -> {
                status = HttpStatus.UNAUTHORIZED
                value["code"] = status.value()
                value["message"] = HttpStatus.UNAUTHORIZED.reasonPhrase
            }
        }
        return ResponseEntity(value, status)
    }

    /**
     * Refresh Token 으로 Access Token 재발행
     */
    fun createAccessTokenByRefreshToken(request: HttpServletRequest): ResponseEntity<*> {
        if (!this.paramsValid(request, ApiConstants.TokenType.REFRESH_TOKEN.code)) {
            throw AliceException(
                AliceErrorConstants.ERR_00001,
                AliceErrorConstants.ERR_00001.message + "[Parameter Error]"
            )
        }
        val refreshToken = request.getHeader("refresh_token")

        val status: HttpStatus?
        val value = LinkedHashMap<String, Any>()
        val refreshTokenEntity = apiTokenRepository.findRefreshToken(refreshToken)
        when (refreshTokenEntity) {
            null -> {
                status = HttpStatus.NO_CONTENT
                value["code"] = status.value()
                value["message"] = HttpStatus.NO_CONTENT.reasonPhrase
            }
            else -> {
                // Refresh Token 만료기간 체크
                val refreshTokenExpires =
                    refreshTokenEntity.createDt.plusSeconds(refreshTokenEntity.refreshTokenExpiresIn.toLong())
                if (LocalDateTime.now() < refreshTokenExpires) {
                    var apiTokenEntity = ApiTokenEntity(
                        apiId = "",
                        accessToken = AliceUtil().getUUID(),
                        refreshToken = AliceUtil().getUUID(),
                        createDt = LocalDateTime.now(),
                        expiresIn = ApiConstants.API_EXPIRES_IN,
                        refreshTokenExpiresIn = ApiConstants.API_REFRESH_TOKEN_EXPIRES_IN,
                        requestUserKey = refreshTokenEntity.requestUserKey
                    )
                    apiTokenEntity = apiTokenRepository.save(apiTokenEntity)
                    value["access_token"] = apiTokenEntity.accessToken
                    value["expires_in"] = apiTokenEntity.expiresIn
                    value["refresh_token"] = apiTokenEntity.refreshToken
                    value["refresh_token_expires_in"] = apiTokenEntity.refreshTokenExpiresIn
                    status = HttpStatus.OK
                } else {
                    status = HttpStatus.UNAUTHORIZED
                    value["code"] = status.value()
                    value["message"] = status.reasonPhrase
                }
            }
        }
        return ResponseEntity(value, status)
    }

    /**
     * Token 발행시 필수 값 확인
     */
    private fun paramsValid(request: HttpServletRequest, type: String): Boolean {
        var returnValue = false
        when (type) {
            ApiConstants.TokenType.ACCESS_TOKEN.code -> {
                if (!request.getHeader("userId").isNullOrEmpty() && !request.getHeader("password").isNullOrEmpty()) {
                    returnValue = true
                }
            }
            ApiConstants.TokenType.REFRESH_TOKEN.code -> {
                if (!request.getHeader("refresh_token").isNullOrEmpty()) {
                    returnValue = true
                }
            }
        }
        return returnValue
    }

    /**
     * Access Token 조회
     */
    fun getAccessToken(authorization: String): ApiTokenEntity? {
        val accessToken = authorization.replace("Bearer ", "")
        return apiTokenRepository.findAccessToken(accessToken)
    }
}
