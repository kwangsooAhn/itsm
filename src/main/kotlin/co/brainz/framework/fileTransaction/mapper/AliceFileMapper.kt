package co.brainz.framework.fileTransaction.mapper

import co.brainz.framework.fileTransaction.dto.AliceFileNameExtensionDto
import co.brainz.framework.fileTransaction.entity.AliceFileNameExtensionEntity
import org.mapstruct.Mapper

@Mapper
interface AliceFileMapper {
    fun toAliceFileNameExtensionDto(fileNameExtensionEntity: AliceFileNameExtensionEntity): AliceFileNameExtensionDto
}
