package co.brainz.itsm.faq.dto

import java.io.Serializable
import java.time.LocalDateTime

data class FaqDto(
    var faqId: String = "",
    var faqGroup: String = "",
    var faqTitle: String = "",
    var faqContent: String = "",
    var createDt: LocalDateTime = LocalDateTime.now(),
    var createUserkey: String?,
    var updateDt: LocalDateTime = LocalDateTime.now(),
    var updateUserkey: String?,
    var fileSeq: List<Long>?
) : Serializable
