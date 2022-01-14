/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.dto

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SchedulerHistorySearchCondition(
    var taskId: String = "",
    var searchResult: Boolean?,
    var searchFromDt: String? = "",
    var searchToDt: String? = "",
    var offset: Long,
    var isScroll: Boolean = false,
    var isModalOpen: Boolean? = false
) : Serializable {
    val formattedFromDt: LocalDateTime? = LocalDateTime.parse(searchFromDt, DateTimeFormatter.ISO_DATE_TIME)
    val formattedToDt: LocalDateTime? = LocalDateTime.parse(searchToDt, DateTimeFormatter.ISO_DATE_TIME)
}