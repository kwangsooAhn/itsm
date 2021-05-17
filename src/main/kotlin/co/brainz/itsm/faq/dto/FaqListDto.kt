/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.dto

import java.io.Serializable
import java.time.LocalDateTime

data class FaqListDto(
    var faqId: String = "",
    var faqGroup: String = "",
    var faqTitle: String = "",
    var faqContent: String = "",
    var createDt: LocalDateTime? = null,
    var createUserName: String? = ""
) : Serializable
