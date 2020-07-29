package co.brainz.itsm.faq.dto

import java.io.Serializable
import java.time.LocalDateTime

data class FaqDto(
    var faqId: String = "",
    var faqGroup: String = "",
    var faqTitle: String = "",
    var faqContent: String = "",
    var createDt: LocalDateTime = LocalDateTime.now(),
    var createUserKey: String?,
    var updateDt: LocalDateTime = LocalDateTime.now(),
    var updateUserKey: String?,
    var fileSeq: List<Long>?,
    var delFileSeq: List<Long>?
) : Serializable
