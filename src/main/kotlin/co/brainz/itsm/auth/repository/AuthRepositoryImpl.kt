/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.QAliceAuthEntity
import co.brainz.itsm.auth.dto.AuthListDto
import co.brainz.itsm.auth.dto.AuthSearchCondition
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AuthRepositoryImpl : QuerydslRepositorySupport(
    AliceAuthEntity::class.java
), AuthRepositoryCustom {

    override fun findAuthSearch(authSearchCondition: AuthSearchCondition): Page<AuthListDto> {
        val auth = QAliceAuthEntity.aliceAuthEntity
        val pageable = Pageable.unpaged()
        val query = from(auth)
            .select(
                Projections.constructor(
                    AuthListDto::class.java,
                    auth.authId,
                    auth.authName,
                    auth.authDesc
                )
            )
            .where(
                super.likeIgnoreCase(auth.authName, authSearchCondition.searchValue)
                    ?.or(super.likeIgnoreCase(auth.authDesc, authSearchCondition.searchValue))
            )
            .orderBy(auth.authName.asc())
        val totalCount = query.fetch().size

            if(authSearchCondition != null) {
                query.limit(authSearchCondition.contentNumPerPage)
                    .offset((authSearchCondition.pageNum - 1) * authSearchCondition.contentNumPerPage)
            }

        return PageImpl<AuthListDto>(query.fetch(), pageable, totalCount.toLong())
    }
}
