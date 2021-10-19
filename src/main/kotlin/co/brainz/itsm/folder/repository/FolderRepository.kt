package co.brainz.itsm.folder.repository

import co.brainz.itsm.folder.entity.WfFolderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FolderRepository : JpaRepository<WfFolderEntity, String>, FolderRepositoryCustom
