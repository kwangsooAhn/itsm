package co.brainz.framework.numbering.mapper

import co.brainz.framework.numbering.dto.AliceNumberingRuleDto
import co.brainz.framework.numbering.entity.AliceNumberingRuleEntity
import org.mapstruct.Mapper

@Mapper
interface AliceNumberingRuleMapper {
    fun toAliceNumberingRuleDto(aliceNumberingRuleEntity: AliceNumberingRuleEntity): AliceNumberingRuleDto
}
