package co.brainz.workflow.folder.repository

import co.brainz.workflow.folder.entity.WfFolderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfFolderRepository : JpaRepository<WfFolderEntity, String>, WfFolderRepositoryCustom
