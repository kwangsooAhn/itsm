package co.brainz.framework.fileTransaction.repository

import co.brainz.framework.fileTransaction.entity.FileLocEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FileLocRepository : JpaRepository<FileLocEntity, Long> {

    @Query("SELECT t FROM FileLocEntity t WHERE t.task = :task")
    fun findAllByTask(task: String): MutableList<FileLocEntity>

//    @Query("update FileLocEntity t set t.upload = true where t.fileSeq = :fileSeq")
//    @Modifying
//    fun updateUploadFlag(fileSeq: Long): MutableList<FileLocEntity>

    override fun getOne(fileSeq: Long): FileLocEntity
    override fun findAll(): MutableList<FileLocEntity>
    //override fun deleteById(fileSeq: Long?)
}