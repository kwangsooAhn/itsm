/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.archive.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.resourceManager.entity.QAliceFileLocEntity
import co.brainz.framework.resourceManager.entity.QAliceFileOwnMapEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.archive.dto.ArchiveListDto
import co.brainz.itsm.archive.dto.ArchiveSearchCondition
import co.brainz.itsm.archive.entity.ArchiveEntity
import co.brainz.itsm.archive.entity.QArchiveEntity
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.portal.dto.PortalTopDto
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ArchiveRepositoryImpl : QuerydslRepositorySupport(ArchiveEntity::class.java), ArchiveRepositoryCustom {

    override fun findArchiveEntityList(archiveSearchCondition: ArchiveSearchCondition): PagingReturnDto {
        val archive = QArchiveEntity.archiveEntity
        val fileMap = QAliceFileOwnMapEntity.aliceFileOwnMapEntity
        val fileLoc = QAliceFileLocEntity.aliceFileLocEntity
        val user = QAliceUserEntity.aliceUserEntity
        val code = QCodeEntity.codeEntity

        val query = from(archive).distinct()
            .leftJoin(fileMap).on(archive.archiveId.eq(fileMap.ownId))
            .leftJoin(fileLoc).on(fileMap.fileLocEntity.fileSeq.eq(fileLoc.fileSeq))
            .select(
                Projections.constructor(
                    ArchiveListDto::class.java,
                    archive.archiveId,
                    archive.archiveSeq,
                    archive.archiveCategory,
                    code.codeName.`as`("archiveCategoryName"),
                    archive.archiveTitle,
                    archive.views,
                    Expressions.numberPath(Long::class.java, "0"),
                    archive.createDt,
                    archive.createUser.userName
                )
            )
            .leftJoin(code).on(code.code.eq(archive.archiveCategory))
            .innerJoin(archive.createUser, user)
            .where(builder(archiveSearchCondition, archive, fileLoc))
            .orderBy(archive.archiveSeq.desc())
        if (archiveSearchCondition.isPaging) {
            query.limit(archiveSearchCondition.contentNumPerPage)
            query.offset((archiveSearchCondition.pageNum - 1) * archiveSearchCondition.contentNumPerPage)
        }

        val countQuery = from(archive)
            .select(archive.count())
            .leftJoin(fileMap).on(archive.archiveId.eq(fileMap.ownId))
            .leftJoin(fileLoc).on(fileMap.fileLocEntity.fileSeq.eq(fileLoc.fileSeq))
            .where(builder(archiveSearchCondition, archive, fileLoc))

        if (archiveSearchCondition.category?.isNotEmpty() == true) {
            countQuery.where(archive.archiveCategory.eq(archiveSearchCondition.category))
        }

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    override fun findArchiveTopList(limit: Long): List<PortalTopDto> {
        val archive = QArchiveEntity.archiveEntity
        val fileMap = QAliceFileOwnMapEntity.aliceFileOwnMapEntity
        val fileLoc = QAliceFileLocEntity.aliceFileLocEntity

        return from(archive).distinct()
            .select(
                Projections.constructor(
                    PortalTopDto::class.java,
                    archive.archiveId,
                    archive.archiveTitle,
                    archive.archiveCategory,
                    archive.createDt
                )
            )
            .leftJoin(fileMap).on(archive.archiveId.eq(fileMap.ownId))
            .leftJoin(fileLoc).on(fileMap.fileLocEntity.fileSeq.eq(fileLoc.fileSeq))
            .orderBy(archive.createDt.desc())
            .limit(limit)
            .fetch()
    }

    override fun findArchive(archiveId: String): ArchiveEntity {
        val archive = QArchiveEntity.archiveEntity
        return from(archive)
            .innerJoin(archive.createUser).fetchJoin()
            .leftJoin(archive.updateUser).fetchJoin()
            .where(archive.archiveId.eq(archiveId))
            .fetchOne()
    }

    private fun builder(
        archiveSearchCondition: ArchiveSearchCondition,
        archive: QArchiveEntity,
        fileLoc: QAliceFileLocEntity
    ): BooleanBuilder {
        val builder = BooleanBuilder()
        if (archiveSearchCondition.category?.isNotEmpty() == true) {
            builder.and(archive.archiveCategory.eq(archiveSearchCondition.category))
        }
        builder.and(
            super.likeIgnoreCase(
                archive.archiveTitle, archiveSearchCondition.searchValue
            )?.or(super.likeIgnoreCase(fileLoc.originName, archiveSearchCondition.searchValue))
                ?.or(super.likeIgnoreCase(archive.createUser.userName, archiveSearchCondition.searchValue))
        )
        builder.and(archive.createDt.goe(archiveSearchCondition.formattedFromDt))
        builder.and(archive.createDt.lt(archiveSearchCondition.formattedToDt))
        return builder
    }
}
