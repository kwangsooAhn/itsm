/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.scheduler.dto

import java.io.Serializable

data class SchedulerSearchDto(
    var offset: Long = 0,
    var search: String? = ""
) : Serializable
