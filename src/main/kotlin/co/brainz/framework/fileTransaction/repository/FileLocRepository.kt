package co.brainz.framework.fileTransaction.repository

import co.brainz.framework.fileTransaction.entity.FileLocEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FileLocRepository : JpaRepository<FileLocEntity, Long> {
    override fun getOne(fileSeq: Long): FileLocEntity
    override fun findAll(): MutableList<FileLocEntity>
    fun findAllByUploaded(uploaded: Boolean): MutableList<FileLocEntity>
}
