package co.brainz.framework.fileTransaction.mapper

import co.brainz.framework.fileTransaction.dto.AliceFileNameExtensionDto
import co.brainz.framework.fileTransaction.entity.AliceFileNameExtensionEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface AliceFileMapper {
    @Mappings(
        Mapping(target = "copy", ignore = true)
    )
    fun toAliceFileNameExtensionDto(fileNameExtensionEntity: AliceFileNameExtensionEntity): AliceFileNameExtensionDto
}
