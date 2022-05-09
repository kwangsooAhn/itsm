package co.brainz.itsm.archive.mapper

import co.brainz.itsm.archive.dto.ArchiveDto
import co.brainz.itsm.archive.entity.ArchiveEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface ArchiveMapper {
    @Mappings(
        Mapping(source = "createUser.userName", target = "createUserName"),
        Mapping(source = "updateUser.userName", target = "updateUserName"),
        Mapping(target = "fileSeqList", ignore = true),
        Mapping(target = "archiveCategoryName", ignore = true),
        Mapping(target = "delFileSeqList", ignore = true)
    )
    fun toArchiveDto(archiveEntity: ArchiveEntity): ArchiveDto

    @Mappings(
        Mapping(target = "createDt", ignore = true),
        Mapping(target = "createUser", ignore = true),
        Mapping(target = "updateDt", ignore = true),
        Mapping(target = "updateUser", ignore = true)
    )
    fun toArchiveEntity(archiveDto: ArchiveDto): ArchiveEntity
}
