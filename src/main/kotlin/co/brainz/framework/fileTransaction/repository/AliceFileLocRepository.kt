package co.brainz.framework.fileTransaction.repository

import co.brainz.framework.fileTransaction.entity.AliceFileLocEntity
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceFileLocRepository : JpaRepository<AliceFileLocEntity, Long> {
    override fun getOne(fileSeq: Long): AliceFileLocEntity

    override fun findAll(): MutableList<AliceFileLocEntity>

    /**
     * [dateTime] 이 지난 [uploaded] 상태의 파일을 조회한다.
     */
    fun findByUploadedAndCreateDtLessThan(
        uploaded: Boolean = false,
        dateTime: LocalDateTime
    ): MutableList<AliceFileLocEntity>
}
