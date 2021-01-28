package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.cmdb.ciClass.entity.QCIClassAttributeMapEntity
import co.brainz.cmdb.ciClass.entity.QCIClassEntity
import co.brainz.cmdb.provider.dto.CIClassToAttributeDto
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIClassRepositoryImpl : QuerydslRepositorySupport(CIClassEntity::class.java), CIClassRepositoryCustom {
    override fun findClassList(search: String): QueryResults<CIClassEntity> {
        val ciClass = QCIClassEntity.cIClassEntity
        return from(ciClass)
            .select(ciClass)
            .where(
                super.like(ciClass.className, search)
                    ?.or(super.like(ciClass.classDesc, search))
            ).orderBy(ciClass.classLevel.asc(), ciClass.className.asc())
            .fetchResults()
    }

    override fun findClassToAttributeList(classList: MutableList<String>): List<CIClassToAttributeDto>? {
        val ciClassAttributeMap = QCIClassAttributeMapEntity.cIClassAttributeMapEntity
        val query = from(ciClassAttributeMap)
            .select(
                Projections.constructor(
                    CIClassToAttributeDto::class.java,
                    ciClassAttributeMap.ciClass.classId,
                    ciClassAttributeMap.ciAttribute.attributeId,
                    ciClassAttributeMap.ciAttribute.attributeName,
                    ciClassAttributeMap.attributeOrder
                )
            )
            .where(
                ciClassAttributeMap.ciClass.classId.`in`(classList)
            ).orderBy(ciClassAttributeMap.attributeOrder.asc())
        val result = query.fetchResults()
        val ciClassToAttributeList = mutableListOf<CIClassToAttributeDto>()
        for (data in result.results) {
            ciClassToAttributeList.add(data)
        }
        return ciClassToAttributeList.toList()
    }
}
