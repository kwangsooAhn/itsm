package co.brainz.itsm.auth.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.auth.dto.AuthListDto
import com.querydsl.core.QueryResults

interface AuthRepositoryCustom : AliceRepositoryCustom {

    fun findAuthSearch(search: String): QueryResults<AuthListDto>
}
