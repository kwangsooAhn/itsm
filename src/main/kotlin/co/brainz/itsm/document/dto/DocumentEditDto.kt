/*
 * Copyright 2020 BrainzCompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.dto

import java.io.Serializable
import java.time.LocalDateTime

data class DocumentEditDto(
    var documentId: String = "",
    var documentType: String = "",
    val documentName: String = "",
    val documentDesc: String? = null,
    var documentStatus: String? = null,
    var processId: String = "",
    var formId: String = "",
    val documentNumberingRuleId: String = "",
    val documentColor: String? = "",
    var documentGroup: String = "",
    var documentRoles: MutableSet<String>? = null,
    var apiEnable: Boolean = false,
    var documentLinkUrl: String? = "",
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable
