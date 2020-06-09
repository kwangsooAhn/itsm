package co.brainz.itsm.download.repository

import co.brainz.itsm.download.entity.DownloadEntity
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DownloadRepository : JpaRepository<DownloadEntity, String> {
    @Query(
        "SELECT DISTINCT d FROM DownloadEntity d " +
                "LEFT JOIN AliceFileOwnMapEntity m ON d.downloadId = m.ownId LEFT JOIN m.fileLocEntity l " +
                "WHERE d.downloadCategory = :category " +
                "AND (LOWER(d.downloadTitle) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR LOWER(d.createUser.userName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR LOWER(COALESCE(l.originName, '')) LIKE LOWER(CONCAT('%', :search, '%'))) " +
                "AND d.createDt BETWEEN :fromDt AND :toDt ORDER BY d.downloadSeq DESC"
    )
    fun findByDownloadList(
        category: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime
    ): List<DownloadEntity>

    @Query(
        "SELECT DISTINCT d FROM DownloadEntity d " +
                "LEFT JOIN AliceFileOwnMapEntity m ON d.downloadId = m.ownId LEFT JOIN m.fileLocEntity l " +
                "WHERE (LOWER(d.downloadTitle) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR LOWER(d.createUser.userName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR LOWER(COALESCE(l.originName, '')) LIKE LOWER(CONCAT('%', :search, '%'))) " +
                "AND d.createDt BETWEEN :fromDt AND :toDt ORDER BY d.downloadSeq DESC"
    )
    fun findByDownloadList(search: String, fromDt: LocalDateTime, toDt: LocalDateTime): List<DownloadEntity>
}
