package co.brainz.framework.fileTransaction.repository

import co.brainz.framework.fileTransaction.entity.AliceFileLocEntity
import co.brainz.framework.fileTransaction.entity.AliceFileOwnMapEntity
import co.brainz.framework.fileTransaction.entity.idclass.AliceFileCompositeKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AliceFileOwnMapRepository : JpaRepository<AliceFileOwnMapEntity, AliceFileCompositeKey> {
    fun findAllByOwnId(ownId: String): List<AliceFileOwnMapEntity>
    @Query("select f from AliceFileOwnMapEntity f inner join fetch f.fileLocEntity where f.ownId = :ownId and f.fileLocEntity.uploaded = :fileLocEntityUploaded")
    fun findAllByOwnIdAndFileLocEntityUploaded(ownId: String, fileLocEntityUploaded: Boolean): List<AliceFileOwnMapEntity>
    fun findByFileLocEntityFileSeq(fileSeq: Long): AliceFileOwnMapEntity
    fun deleteByFileLocEntity(aliceFileLocEntity: AliceFileLocEntity)
}
