package co.brainz.framework.fileTransaction.repository

import co.brainz.framework.fileTransaction.entity.AliceFileLocEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceFileLocRepository : JpaRepository<AliceFileLocEntity, Long> {
    override fun getOne(fileSeq: Long): AliceFileLocEntity
    override fun findAll(): MutableList<AliceFileLocEntity>
}
