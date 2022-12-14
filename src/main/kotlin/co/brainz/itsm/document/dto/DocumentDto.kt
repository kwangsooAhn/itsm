/*
 * Copyright 2020 BrainzCompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.document.dto

import java.io.Serializable
import java.time.LocalDateTime

data class DocumentDto(
    val documentId: String = "",
    val documentType: String = "",
    val documentName: String = "",
    val documentDesc: String? = null,
    var documentStatus: String? = null,
    var processId: String = "",
    var formId: String = "",
    val documentNumberingRuleId: String = "",
    val documentColor: String? = "",
    var documentGroup: String? = null,
    var apiEnable: Boolean = false,
    var documentLinkUrl: String? = "",
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable
