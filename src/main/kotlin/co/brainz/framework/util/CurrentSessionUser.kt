/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.util

import co.brainz.framework.auth.dto.AliceUserDto
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CurrentSessionUser {
    fun getUserDto(): AliceUserDto? = SecurityContextHolder.getContext().authentication.details as? AliceUserDto
    fun getUserKey(): String = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!.userKey
    fun getUserId(): String = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!.userId
    fun getEmail(): String = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!.email
}
