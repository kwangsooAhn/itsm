/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.archive.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.archive.dto.ArchiveSearchCondition
import co.brainz.itsm.archive.entity.ArchiveEntity
import co.brainz.itsm.portal.dto.PortalTopDto

interface ArchiveRepositoryCustom : AliceRepositoryCustom {

    fun findArchiveEntityList(archiveSearchCondition: ArchiveSearchCondition): PagingReturnDto

    fun findArchiveTopList(limit: Long): List<PortalTopDto>

    fun findArchive(archiveId: String): ArchiveEntity
}
