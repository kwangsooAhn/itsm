/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.ci.entity.QCIEntity
import co.brainz.cmdb.ciClass.entity.QCIClassEntity
import co.brainz.cmdb.ciTag.entity.QCITagEntity
import co.brainz.cmdb.ciType.entity.QCITypeEntity
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CIsDto
import co.brainz.itsm.cmdb.ci.entity.QCIComponentDataEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIRepositoryImpl : QuerydslRepositorySupport(CIEntity::class.java), CIRepositoryCustom {
    /**
     * CI 목록 조회.
     */
    override fun findCIList(search: String, offset: Long?, tags: List<String>, flag: String): MutableList<CIsDto> {
        val ci = QCIEntity.cIEntity
        val cmdbType = QCITypeEntity.cITypeEntity
        val cmdbClass = QCIClassEntity.cIClassEntity
        val cmdbTag = QCITagEntity.cITagEntity
        val wfComponentCIData = QCIComponentDataEntity.cIComponentDataEntity
        val wfInstance = QWfInstanceEntity.wfInstanceEntity

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
            .where(
                super.like(ci.ciName, search)
                    ?.or(super.like(ci.ciNo, search))
                    ?.or(super.like(ci.ciTypeEntity.typeName, search))
                    ?.or(super.like(ci.ciClassEntity.className, search))
                    ?.or(super.like(ci.ciDesc, search))
            ).orderBy(ci.ciName.asc())
        if (tags.isNotEmpty()) {
            query.where(
                ci.ciId.`in`(
                    JPAExpressions
                        .select(cmdbTag.ci.ciId)
                        .from(cmdbTag)
                        .where(
                            cmdbTag.tagName.`in`(tags)
                        )
                )
            )
        }
        if (flag == "component") {
            query.where(
                ci.ciStatus.eq(RestTemplateConstants.CIStatus.STATUS_USE.code)
                    .and(
                        ci.ciId.notIn(
                            JPAExpressions
                                .select(wfComponentCIData.ciId)
                                .from(wfComponentCIData)
                                .innerJoin(wfInstance).on(wfComponentCIData.instanceId.eq(wfInstance.instanceId))
                        )
                    )
            )
        }
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

    /**
     * 동일 CI_NO에서 마지막 번호 조회.
     */
    override fun getLastCiByCiNo(ciNoPrefix: String): CIEntity? {
        val ciEntity = QCIEntity.cIEntity
        return from(ciEntity)
            .where(
                ciEntity.ciNo.like("$ciNoPrefix%")
            )
            .orderBy(ciEntity.ciNo.desc())
            .fetchFirst()
    }
}
