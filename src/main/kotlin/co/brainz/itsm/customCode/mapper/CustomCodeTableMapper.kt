package co.brainz.itsm.customCode.mapper

import co.brainz.itsm.customCode.dto.CustomCodeTableDto
import co.brainz.itsm.customCode.entity.CustomCodeTableEntity
import org.mapstruct.Mapper

@Mapper
interface CustomCodeTableMapper {
    fun toCustomCodeTableDto(customCodeTableEntity: CustomCodeTableEntity): CustomCodeTableDto
}