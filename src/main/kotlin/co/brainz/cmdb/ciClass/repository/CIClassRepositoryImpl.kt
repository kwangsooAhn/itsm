package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CmdbClassEntity
import co.brainz.cmdb.ciClass.entity.QCmdbClassEntity
import co.brainz.cmdb.provider.dto.CmdbClassListDto
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIClassRepositoryImpl : QuerydslRepositorySupport(CmdbClassEntity::class.java), CIClassRepositoryCustom {
    override fun findClassList(search: String, offset: Long?): List<CmdbClassListDto> {
        val cmdbClass = QCmdbClassEntity.cmdbClassEntity
        val query = from(cmdbClass)
            .select(
                Projections.constructor(
                    CmdbClassListDto::class.java,
                    cmdbClass.classId,
                    cmdbClass.className,
                    cmdbClass.classDesc,
                    cmdbClass.pClassId
                )
            )
            .where(
                super.likeIgnoreCase(cmdbClass.className, search)
                    ?.or(super.likeIgnoreCase(cmdbClass.classDesc, search))

            ).orderBy(cmdbClass.className.asc())
        if (offset != null) {
            query.limit(ItsmConstants.SEARCH_DATA_COUNT)
                .offset(offset)
        }
        val result = query.fetchResults()
        val cmdbClassList = mutableListOf<CmdbClassListDto>()
        for (data in result.results) {
            cmdbClassList.add(data)
        }
        return cmdbClassList.toList()
    }
}
