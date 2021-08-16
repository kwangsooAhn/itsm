/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.fileTransaction.entity.QAliceFileLocEntity
import co.brainz.framework.fileTransaction.entity.QAliceFileOwnMapEntity
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.download.dto.DownloadListDto
import co.brainz.itsm.download.dto.DownloadListReturnDto
import co.brainz.itsm.download.dto.DownloadSearchCondition
import co.brainz.itsm.download.entity.DownloadEntity
import co.brainz.itsm.download.entity.QDownloadEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class DownloadRepositoryImpl : QuerydslRepositorySupport(DownloadEntity::class.java), DownloadRepositoryCustom {

    override fun findDownloadEntityList(downloadSearchCondition: DownloadSearchCondition): DownloadListReturnDto {
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
        if (downloadSearchCondition.category?.isNotEmpty() == true) {
            query.where(download.downloadCategory.eq(downloadSearchCondition.category))
        }

        query.where(
            super.like(
                download.downloadTitle, downloadSearchCondition.searchValue
            )?.or(super.like(fileLoc.originName, downloadSearchCondition.searchValue))
                ?.or(super.like(download.createUser.userName, downloadSearchCondition.searchValue)),
            download.createDt.goe(downloadSearchCondition.formattedFromDt), download.createDt.lt(downloadSearchCondition.formattedToDt)
        ).orderBy(download.downloadSeq.desc())
            .limit(downloadSearchCondition.contentNumPerPage)
            .offset((downloadSearchCondition.pageNum - 1) * downloadSearchCondition.contentNumPerPage)

        val result = query.fetchResults()
        return DownloadListReturnDto(
            data = result.results,
            paging = AlicePagingData(
                totalCount = result.total,
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
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
