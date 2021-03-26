package co.brainz.framework.fileTransaction.repository

import co.brainz.framework.fileTransaction.entity.AliceFileLocEntity
import co.brainz.framework.fileTransaction.entity.AliceFileOwnMapEntity
import co.brainz.framework.fileTransaction.entity.idclass.AliceFileCompositeKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AliceFileOwnMapRepository : JpaRepository<AliceFileOwnMapEntity, AliceFileCompositeKey>,
    AliceFileOwnMapRepositoryCustom {
    fun findAllByOwnId(ownId: String): List<AliceFileOwnMapEntity>
    fun findByFileLocEntityFileSeq(fileSeq: Long): AliceFileOwnMapEntity
    fun deleteByFileLocEntity(aliceFileLocEntity: AliceFileLocEntity)
}
