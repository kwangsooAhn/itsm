package co.brainz.workflow.engine.folder.repository

import co.brainz.workflow.engine.folder.entity.WfFolderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfFolderRepository : JpaRepository<WfFolderEntity, String> {
    fun findByFolderId(folderId: String): WfFolderEntity

}