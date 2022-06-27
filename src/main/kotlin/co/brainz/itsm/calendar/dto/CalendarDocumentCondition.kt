/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.calendar.dto

import java.io.Serializable

data class CalendarDocumentCondition(
    val range: Range? = null,
    val instanceId: String? = null
) : Serializable
