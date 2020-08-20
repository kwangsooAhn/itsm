/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.repository

import co.brainz.framework.fileTransaction.entity.QAliceFileLocEntity
import co.brainz.framework.fileTransaction.entity.QAliceFileOwnMapEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.download.dto.DownloadListDto
import co.brainz.itsm.download.entity.DownloadEntity
import co.brainz.itsm.download.entity.QDownloadEntity
import com.querydsl.core.QueryResults
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class DownloadRepositoryImpl : QuerydslRepositorySupport(DownloadEntity::class.java), DownloadRepositoryCustom {

    override fun findDownloadEntityList(
        category: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): List<DownloadListDto> {
        val download = QDownloadEntity.downloadEntity
        val fileMap = QAliceFileOwnMapEntity.aliceFileOwnMapEntity
        val fileLoc = QAliceFileLocEntity.aliceFileLocEntity

        val query = from(download).distinct()
            .leftJoin(fileMap).on(download.downloadId.eq(fileMap.ownId))
            .leftJoin(fileLoc).on(fileMap.fileLocEntity.fileSeq.eq(fileLoc.fileSeq))
        if (category.isNotEmpty()) {
            query.where(download.downloadCategory.eq(category))
        }

        query.where(
            download.downloadTitle.containsIgnoreCase(search)
                .or(fileLoc.originName.containsIgnoreCase(search))
                .or(download.createUser.userName.containsIgnoreCase(search))
                .and(download.createDt.goe(fromDt))
                .and(download.createDt.lt(toDt))
        ).orderBy(download.downloadSeq.desc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)

        val queryResult: QueryResults<DownloadEntity> = query.fetchResults()
        val downloadList = mutableListOf<DownloadListDto>()
        for (data in queryResult.results) {
            val downloadDto = DownloadListDto(
                downloadId = data.downloadId,
                downloadSeq = data.downloadSeq,
                downloadCategory = data.downloadCategory,
                downloadTitle = data.downloadCategory,
                views = data.views,
                totalCount = queryResult.total,
                createDt = data.createDt,
                createUserName = data.createUser?.userName,
                updateDt = data.updateDt,
                updateUserName = data.updateUser?.userName
            )
            downloadList.add(downloadDto)
        }
        return downloadList.toList()
    }

    override fun findDownloadTopList(limit: Long): List<DownloadEntity> {
        val download = QDownloadEntity.downloadEntity
        val fileMap = QAliceFileOwnMapEntity.aliceFileOwnMapEntity
        val fileLoc = QAliceFileLocEntity.aliceFileLocEntity

        return from(download).distinct()
            .leftJoin(fileMap).on(download.downloadId.eq(fileMap.ownId))
            .leftJoin(fileLoc).on(fileMap.fileLocEntity.fileSeq.eq(fileLoc.fileSeq))
            .orderBy(download.downloadSeq.desc())
            .limit(limit)
            .fetch()
    }
}
