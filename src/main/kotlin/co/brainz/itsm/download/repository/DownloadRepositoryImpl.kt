/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.fileTransaction.entity.QAliceFileLocEntity
import co.brainz.framework.fileTransaction.entity.QAliceFileOwnMapEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.download.dto.DownloadListDto
import co.brainz.itsm.download.dto.DownloadListReturnDto
import co.brainz.itsm.download.entity.DownloadEntity
import co.brainz.itsm.download.entity.QDownloadEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
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
    ): DownloadListReturnDto {
        val download = QDownloadEntity.downloadEntity
        val fileMap = QAliceFileOwnMapEntity.aliceFileOwnMapEntity
        val fileLoc = QAliceFileLocEntity.aliceFileLocEntity
        val user = QAliceUserEntity.aliceUserEntity

        val query = from(download).distinct()
            .leftJoin(fileMap).on(download.downloadId.eq(fileMap.ownId))
            .leftJoin(fileLoc).on(fileMap.fileLocEntity.fileSeq.eq(fileLoc.fileSeq))
            .select(
                Projections.constructor(
                    DownloadListDto::class.java,
                    download.downloadId,
                    download.downloadSeq,
                    download.downloadCategory,
                    download.downloadTitle,
                    download.views,
                    Expressions.numberPath(Long::class.java, "0"),
                    download.createDt,
                    download.createUser.userName
                )
            )
            .innerJoin(download.createUser, user)
        if (category.isNotEmpty()) {
            query.where(download.downloadCategory.eq(category))
        }

        query.where(
            super.like(
                download.downloadTitle, search
            )?.or(super.like(fileLoc.originName, search))
                ?.or(super.like(download.createUser.userName, search)),
            download.createDt.goe(fromDt), download.createDt.lt(toDt)
        ).orderBy(download.downloadSeq.desc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)

        val result = query.fetchResults()
        val downloadList = mutableListOf<DownloadListDto>()
        for (data in result.results) {
            data.totalCount = result.total
            downloadList.add(data)
        }
        return DownloadListReturnDto(
            data = downloadList,
            totalCount = result.total
        )
    }

    override fun findDownloadTopList(limit: Long): List<PortalTopDto> {
        val download = QDownloadEntity.downloadEntity
        val fileMap = QAliceFileOwnMapEntity.aliceFileOwnMapEntity
        val fileLoc = QAliceFileLocEntity.aliceFileLocEntity

        return from(download).distinct()
            .select(
                Projections.constructor(
                    PortalTopDto::class.java,
                    download.downloadId,
                    download.downloadTitle,
                    download.downloadCategory,
                    download.createDt
                )
            )
            .leftJoin(fileMap).on(download.downloadId.eq(fileMap.ownId))
            .leftJoin(fileLoc).on(fileMap.fileLocEntity.fileSeq.eq(fileLoc.fileSeq))
            .orderBy(download.createDt.desc())
            .limit(limit)
            .fetch()
    }

    override fun findDownload(downloadId: String): DownloadEntity {
        val download = QDownloadEntity.downloadEntity
        return from(download)
            .innerJoin(download.createUser).fetchJoin()
            .leftJoin(download.updateUser).fetchJoin()
            .where(download.downloadId.eq(downloadId))
            .fetchOne()
    }
}
