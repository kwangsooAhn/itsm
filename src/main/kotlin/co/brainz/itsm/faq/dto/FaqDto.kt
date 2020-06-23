package co.brainz.itsm.faq.dto

import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZoneId

data class FaqDto(
    var faqId: String = "",
    var faqGroup: String = "",
    var faqTitle: String = "",
    var faqContent: String = "",
    var createDt: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
    var createUserKey: String?,
    var updateDt: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
    var updateUserKey: String?,
    var fileSeq: List<Long>?
) : Serializable
