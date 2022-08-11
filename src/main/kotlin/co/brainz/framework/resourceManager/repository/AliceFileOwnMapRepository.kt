package co.brainz.framework.resourceManager.repository

import co.brainz.framework.resourceManager.entity.AliceFileLocEntity
import co.brainz.framework.resourceManager.entity.AliceFileOwnMapEntity
import co.brainz.framework.resourceManager.entity.idclass.AliceFileCompositeKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceFileOwnMapRepository : JpaRepository<AliceFileOwnMapEntity, AliceFileCompositeKey>,
    AliceFileOwnMapRepositoryCustom {
    fun findAllByOwnId(ownId: String): List<AliceFileOwnMapEntity>
    fun findByFileLocEntityFileSeq(fileSeq: Long): AliceFileOwnMapEntity
    fun deleteByFileLocEntity(aliceFileLocEntity: AliceFileLocEntity)
}
