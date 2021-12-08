/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.provider.dto

import co.brainz.framework.tag.dto.AliceTagDto
import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateInstanceViewDto(
    val tokenId: String,
    val instanceId: String,
    val instanceStartDt: LocalDateTime,
    val instanceEndDt: LocalDateTime?,
    val instanceCreateUser: String,
    val documentName: String,
    val documentDesc: String? = "",
    val documentStatus: String? = null,
    val elementName: String? = "",
    val topics: MutableList<String>?,
    var tags: List<AliceTagDto> = mutableListOf(),
    var createDt: LocalDateTime?,
    val assigneeUserKey: String? = "",
    val assigneeUserName: String? = "",
    val createUserKey: String? = "",
    val createUserName: String? = "",
    val documentId: String,
    val documentNo: String? = null,
    val documentColor: String?,
    val documentType: String,
    val avatarPath: String? = ""
) : Serializable
