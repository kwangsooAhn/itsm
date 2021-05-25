package co.brainz.itsm.portal.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.portal.dto.PortalListReturnDto

interface PortalRepositoryCustom : AliceRepositoryCustom {

    fun findPortalSearchList(searchValue: String, offset: Long): PortalListReturnDto
}
