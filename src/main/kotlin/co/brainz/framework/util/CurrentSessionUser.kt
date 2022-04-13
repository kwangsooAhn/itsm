/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.util

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.dashboard.constants.DashboardConstants
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CurrentSessionUser {
    fun getUserDto(): AliceUserDto? = SecurityContextHolder.getContext().authentication.details as? AliceUserDto
    fun getUserKey(): String = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!.userKey
    fun getUserName(): String = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!.userName
    fun getUserId(): String = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!.userId
    fun getEmail(): String = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!.email
    fun getTimezone(): String = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!.timezone
    fun getUserTemplateId(): String = DashboardConstants.DEFAULT_TEMPLATE_ID

    fun getAuth(): Set<String> {
        val authorises = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!
            .grantedAuthorises ?: emptySet()
        val prefix = "ROLE_"
        val auths = mutableSetOf<String>()
        authorises.iterator().forEach {
            if (!it.authority.startsWith(prefix)) {
                auths.add(it.authority)
            }
        }
        return auths.toSet()
    }

    fun getRoles(): Set<String> {
        val authorises = (SecurityContextHolder.getContext().authentication.details as? AliceUserDto)!!
            .grantedAuthorises ?: emptySet()
        val roles = mutableSetOf<String>()
        val prefix = "ROLE_"
        authorises.iterator().forEach {
           if (it.authority.startsWith(prefix)) {
               roles.add(it.authority.replace(prefix, ""))
           }
        }
        return roles.toSet()
    }
}
