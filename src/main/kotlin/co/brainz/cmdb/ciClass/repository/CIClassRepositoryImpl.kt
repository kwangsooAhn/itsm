package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CmdbClassEntity
import co.brainz.cmdb.ciClass.entity.QCmdbClassAttributeMapEntity
import co.brainz.cmdb.ciClass.entity.QCmdbClassEntity
import co.brainz.cmdb.provider.dto.CmdbClassToAttributeDto
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIClassRepositoryImpl : QuerydslRepositorySupport(CmdbClassEntity::class.java), CIClassRepositoryCustom {
    override fun findClassList(search: String): QueryResults<CmdbClassEntity> {
        val cmdbClass = QCmdbClassEntity.cmdbClassEntity
        return from(cmdbClass)
            .select(cmdbClass)
            .where(
                super.like(cmdbClass.className, search)
                    ?.or(super.like(cmdbClass.classDesc, search))
            ).orderBy(cmdbClass.classLevel.asc(), cmdbClass.className.asc())
            .fetchResults()
    }

    override fun findClassToAttributeList(classList: MutableList<String>): List<CmdbClassToAttributeDto>? {
        val cmdbClassAttributeMap = QCmdbClassAttributeMapEntity.cmdbClassAttributeMapEntity
        val query = from(cmdbClassAttributeMap)
            .select(
                Projections.constructor(
                    CmdbClassToAttributeDto::class.java,
                    cmdbClassAttributeMap.cmdbClass.classId,
                    cmdbClassAttributeMap.cmdbAttribute.attributeId,
                    cmdbClassAttributeMap.cmdbAttribute.attributeName,
                    cmdbClassAttributeMap.attributeOrder
                )
            )
            .where(
                cmdbClassAttributeMap.cmdbClass.classId.`in`(classList)
            ).orderBy(cmdbClassAttributeMap.attributeOrder.asc())
        val result = query.fetchResults()
        val cmdbClassToAttributeList = mutableListOf<CmdbClassToAttributeDto>()
        for (data in result.results) {
            cmdbClassToAttributeList.add(data)
        }
        return cmdbClassToAttributeList.toList()
    }
}
