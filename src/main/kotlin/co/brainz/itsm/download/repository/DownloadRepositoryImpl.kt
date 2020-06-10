package co.brainz.itsm.download.repository

import co.brainz.framework.fileTransaction.entity.QAliceFileLocEntity
import co.brainz.framework.fileTransaction.entity.QAliceFileOwnMapEntity
import co.brainz.itsm.download.entity.DownloadEntity
import co.brainz.itsm.download.entity.QDownloadEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class DownloadRepositoryImpl : QuerydslRepositorySupport(DownloadEntity::class.java), DownloadRepositoryCustom {

    override fun findDownloadEntityList(
        category: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime
    ): List<DownloadEntity> {
        val download = QDownloadEntity.downloadEntity
        val fileMap = QAliceFileOwnMapEntity.aliceFileOwnMapEntity
        val fileLoc = QAliceFileLocEntity.aliceFileLocEntity

        val query =
            from(download).distinct().leftJoin(fileMap).on(download.downloadId.eq(fileMap.ownId)).leftJoin(fileLoc)
                .on(fileMap.fileLocEntity.fileSeq.eq(fileLoc.fileSeq))
        if (category.isNotEmpty()) {
            query.where(download.downloadCategory.containsIgnoreCase(category))
        }

        query.where(
            download.downloadTitle.containsIgnoreCase(search)
                .or(fileLoc.originName.containsIgnoreCase(search))
                .or(download.createUser.userName.containsIgnoreCase(search)).and(
                    download.createDt.between(fromDt, toDt)
                )
        ).orderBy(download.downloadSeq.desc())

        return query.fetch()
    }
}
