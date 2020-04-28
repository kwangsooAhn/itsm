package co.brainz.itsm.faq.dto

import java.io.Serializable

data class FaqListDto(
    var faqId: String = "",
    var faqGroup: String = "",
    var faqTitle: String = "",
    var faqContent: String = ""
) : Serializable
