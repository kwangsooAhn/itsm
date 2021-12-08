/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.provider.dto

import java.time.LocalDateTime

data class RestTemplateInstanceExcelDto(
    val documentNo: String? = null,
    val documentName: String,
    val documentDesc: String? = "",
    val documentStatus: String? = null,
    val documentType: String,
    val instanceStartDt: LocalDateTime,
    val instanceEndDt: LocalDateTime?,
    val instanceCreateUser: String
)