package co.brainz.itsm.portal.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.portal.dto.PortalDto

interface PortalRepositoryCustom : AliceRepositoryCustom {

    fun findPortalSearchList(searchValue: String, limit: Long, offset: Long): MutableList<PortalDto>
}
