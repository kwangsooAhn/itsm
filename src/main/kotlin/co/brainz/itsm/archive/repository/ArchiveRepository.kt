package co.brainz.itsm.archive.repository

import co.brainz.itsm.archive.entity.ArchiveEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ArchiveRepository : JpaRepository<ArchiveEntity, String>, ArchiveRepositoryCustom
