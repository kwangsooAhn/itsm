package co.brainz.framework.fileTransaction.repository

import co.brainz.framework.fileTransaction.entity.FileLocEntity
import co.brainz.framework.fileTransaction.entity.FileOwnMapEntity
import co.brainz.framework.fileTransaction.entity.idclass.FileCompositeKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FileOwnMapRepository : JpaRepository<FileOwnMapEntity, FileCompositeKey> {
    fun findAllByOwnId(ownId: String): List<FileOwnMapEntity>
    fun findAllByOwnIdAndFileLocEntityUploaded(ownId: String, fileLocEntityUploaded: Boolean): List<FileOwnMapEntity>
    fun findByFileLocEntityFileSeq(fileSeq: Long): FileOwnMapEntity
    fun deleteByFileLocEntity(fileLocEntity: FileLocEntity)
}
