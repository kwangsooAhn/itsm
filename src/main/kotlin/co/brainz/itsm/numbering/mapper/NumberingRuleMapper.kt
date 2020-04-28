package co.brainz.itsm.numbering.mapper

import co.brainz.itsm.numbering.dto.NumberingRuleDto
import co.brainz.itsm.numbering.entity.NumberingRuleEntity
import org.mapstruct.Mapper

@Mapper
interface NumberingRuleMapper {
    fun toNumberingRuleDto(numberingRuleEntity: NumberingRuleEntity): NumberingRuleDto
}
