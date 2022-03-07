/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.ci.entity.QCIEntity
import co.brainz.cmdb.ciClass.entity.QCIClassEntity
import co.brainz.cmdb.ciType.constants.CITypeConstants
import co.brainz.cmdb.ciType.entity.QCITypeEntity
import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIsDto
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.entity.QAliceTagEntity
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
import co.brainz.itsm.cmdb.ci.entity.QCIComponentDataEntity
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIRepositoryImpl : QuerydslRepositorySupport(CIEntity::class.java), CIRepositoryCustom {

    /**
     * CI 단일 목록 조회.
     */
    override fun findCI(ciId: String): CIsDto {
        val ci = QCIEntity.cIEntity
        val cmdbType = QCITypeEntity.cITypeEntity
        val cmdbClass = QCIClassEntity.cIClassEntity
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
                    cmdbType.typeIcon,
                    ci.ciDesc,
                    ci.interlink,
                    ci.createUser.userKey,
                    ci.createDt,
                    ci.updateUser.userKey,
                    ci.updateDt
                )
            )
            .innerJoin(cmdbType).on(cmdbType.typeId.eq(ci.ciTypeEntity.typeId))
            .innerJoin(cmdbClass).on(cmdbClass.classId.eq(ci.ciTypeEntity.ciClass.classId))
            .where(ci.ciId.eq(ciId))
            .orderBy(ci.ciName.asc())
        return query.fetchOne()
    }

    /**
     * CI 목록 조회.
     */
    override fun findCIList(ciSearchCondition: CISearchCondition): QueryResults<CIsDto> {
        val ci = QCIEntity.cIEntity
        val cmdbType = QCITypeEntity.cITypeEntity
        val cmdbClass = QCIClassEntity.cIClassEntity
        val cmdbTag = QAliceTagEntity.aliceTagEntity
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
                    cmdbType.typeIcon,
                    ci.ciDesc,
                    ci.interlink,
                    ci.createUser.userKey,
                    ci.createDt,
                    ci.updateUser.userKey,
                    ci.updateDt
                )
            )
            .innerJoin(cmdbType).on(cmdbType.typeId.eq(ci.ciTypeEntity.typeId))
            .innerJoin(cmdbClass).on(cmdbClass.classId.eq(ci.ciTypeEntity.ciClass.classId))
        if (ciSearchCondition.typeId != null && ciSearchCondition.typeId != CITypeConstants.CI_TYPE_ROOT_ID) {
            query.where(ci.ciTypeEntity.typeId.eq(ciSearchCondition.typeId))
        }
        query.where(
            (!ci.ciStatus.eq(RestTemplateConstants.CIStatus.STATUS_DELETE.code))
                .and(
                    super.likeIgnoreCase(ci.ciName, ciSearchCondition.searchValue)
                        ?.or(super.likeIgnoreCase(ci.ciNo, ciSearchCondition.searchValue))
                        //?.or(super.likeIgnoreCase(ci.ciTypeEntity.typeName, ciSearchCondition.searchValue))
                        ?.or(super.likeIgnoreCase(cmdbClass.className, ciSearchCondition.searchValue))
                        ?.or(super.likeIgnoreCase(ci.ciDesc, ciSearchCondition.searchValue))
                )
        )
        if (ciSearchCondition.isSearchType) {
            query.where(super.likeIgnoreCase(ci.ciTypeEntity.typeName, ciSearchCondition.searchValue))
        }
        query.orderBy(ci.ciName.asc())
        if (ciSearchCondition.tagArray.isNotEmpty()) {
            query.where(
                ci.ciId.`in`(
                    JPAExpressions
                        .select(cmdbTag.targetId)
                        .from(cmdbTag)
                        .where(
                            cmdbTag.tagValue.`in`(ciSearchCondition.tagArray)
                                .and(cmdbTag.tagType.eq(AliceTagConstants.TagType.CI.code))
                        )
                )
            )
        }
        if (ciSearchCondition.flag == "component") {
            query.where(
                ci.ciId.notIn(
                    JPAExpressions
                        .select(wfComponentCIData.ciId)
                        .from(wfComponentCIData)
                        .innerJoin(wfInstance).on(wfComponentCIData.instanceId.eq(wfInstance.instanceId))
                        .where(wfInstance.instanceStatus.eq(WfInstanceConstants.Status.RUNNING.code))
                )
            )
        } else if (ciSearchCondition.flag == "relation") {
            query.where(!ci.ciId.eq(ciSearchCondition.relationSearch))
        }

        if (ciSearchCondition.isPaging) {
            query.limit(ciSearchCondition.contentNumPerPage)
            query.offset((ciSearchCondition.pageNum - 1) * ciSearchCondition.contentNumPerPage)
        }

        return query.fetchResults()
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

    /**
     * CI Type 기준 전체 건수
     */
    override fun countByTypeId(typeId: String): Long {
        val ciEntity = QCIEntity.cIEntity
        val query = from(ciEntity)
        if (typeId != CITypeConstants.CI_TYPE_ROOT_ID) {
            query.where(ciEntity.ciTypeEntity.typeId.eq(typeId))
        }
        return query.fetchCount()
    }
}
