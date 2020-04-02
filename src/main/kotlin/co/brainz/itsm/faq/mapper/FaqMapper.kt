package co.brainz.itsm.faq.mapper

import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.entity.FaqEntity
import org.mapstruct.Mapper

@Mapper
interface FaqMapper {
    fun toFaqListDto(faqEntity: FaqEntity): FaqListDto
}
