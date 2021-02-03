/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.ci.entity.QCIEntity
import co.brainz.cmdb.ciClass.entity.QCIClassEntity
import co.brainz.cmdb.ciType.entity.QCITypeEntity
import co.brainz.cmdb.provider.dto.CIsDto
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIRepositoryImpl : QuerydslRepositorySupport(CIEntity::class.java), CIRepositoryCustom {
    /**
     * CI 목록 조회.
     */
    override fun findCIList(search: String, offset: Long?, tags: List<String>): MutableList<CIsDto> {
        val ci = QCIEntity.cIEntity
        val cmdbType = QCITypeEntity.cITypeEntity
        val cmdbClass = QCIClassEntity.cIClassEntity
        // val cmdbTag = QCITagEntity.cITagEntity
        val query = from(ci)
            .select(
                Projections.constructor(
                    CIsDto::class.java,
                    ci.ciId,
                    ci.ciNo,
                    ci.ciName,
                    ci.ciStatus,
                    cmdbType.typeId,
                    cmdbType.typeName,
                    cmdbClass.classId,
                    cmdbClass.className,
                    ci.ciIcon,
                    ci.ciDesc,
                    ci.automatic,
                    ci.createUser.userKey,
                    ci.createDt,
                    ci.updateUser.userKey,
                    ci.updateDt,
                    Expressions.numberPath(Long::class.java, "0")
                )
            )
            .innerJoin(cmdbType).on(cmdbType.typeId.eq(ci.ciTypeEntity.typeId))
            .innerJoin(cmdbClass).on(cmdbClass.classId.eq(ci.ciClassEntity.classId))
            // .leftJoin(cmdbTag).on(cmdbTag.ci.ciId.eq(ci.ciId))
            .where(
                super.like(ci.ciName, search)
                    ?.or(super.like(ci.ciNo, search))
                    ?.or(super.like(ci.ciTypeEntity.typeName, search))
                    ?.or(super.like(ci.ciClassEntity.className, search))
                    ?.or(super.like(ci.ciDesc, search))
            ).orderBy(ci.ciName.asc())
        if (offset != null) {
            query.limit(ItsmConstants.SEARCH_DATA_COUNT).offset(offset)
        }
        val result = query.fetchResults()
        val cis = mutableListOf<CIsDto>()
        for (data in result.results) {
            data.totalCount = result.total
            cis.add(data)
        }
        return cis
    }

    /**
     * CI 번호 중복 체크.
     */
    override fun findDuplicateCiNo(ciNo: String): Long {
        val ciEntity = QCIEntity.cIEntity
        val query = from(ciEntity)
            .where(ciEntity.ciNo.eq(ciNo))
        return query.fetchCount()
    }
}
