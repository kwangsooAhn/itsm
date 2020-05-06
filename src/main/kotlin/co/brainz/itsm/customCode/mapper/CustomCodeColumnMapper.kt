package co.brainz.itsm.customCode.mapper

import co.brainz.itsm.customCode.dto.CustomCodeColumnDto
import co.brainz.itsm.customCode.entity.CustomCodeColumnEntity
import org.mapstruct.Mapper

@Mapper
interface CustomCodeColumnMapper {
    fun toCustomCodeColumnDto(customCodeColumnEntity: CustomCodeColumnEntity): CustomCodeColumnDto
}
