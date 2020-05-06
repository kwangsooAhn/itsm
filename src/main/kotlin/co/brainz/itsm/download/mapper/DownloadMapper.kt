package co.brainz.itsm.download.mapper

import co.brainz.itsm.download.dto.DownloadDto
import co.brainz.itsm.download.entity.DownloadEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface DownloadMapper {
    @Mappings(
        Mapping(source = "createUser.userName", target = "createUserName"),
        Mapping(source = "updateUser.userName", target = "updateUserName")
    )
    fun toDownloadDto(downloadEntity: DownloadEntity): DownloadDto

    @Mappings(
        Mapping(target = "createDt", ignore = true),
        Mapping(target = "updateDt", ignore = true)
    )
    fun toDownloadEntity(downloadDto: DownloadDto): DownloadEntity
}
