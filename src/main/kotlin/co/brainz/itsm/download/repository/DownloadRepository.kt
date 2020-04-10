package co.brainz.itsm.download.repository

import co.brainz.itsm.download.entity.DownloadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DownloadRepository: JpaRepository<DownloadEntity, String> {

    @Query("SELECT d FROM DownloadEntity d WHERE d.downloadCategory = :category " +
           "AND (LOWER(d.downloadTitle) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.createUser.userName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND d.createDt BETWEEN :fromDt AND :toDt ORDER BY d.downloadId DESC")
    fun findByDownloadList(category: String, search: String, fromDt: LocalDateTime, toDt: LocalDateTime): List<DownloadEntity>

    @Query("SELECT d FROM DownloadEntity d " +
           "WHERE (LOWER(d.downloadTitle) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(d.createUser.userName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND d.createDt BETWEEN :fromDt AND :toDt ORDER BY d.downloadId DESC")
    fun findByDownloadList(search: String, fromDt: LocalDateTime, toDt: LocalDateTime): List<DownloadEntity>
}