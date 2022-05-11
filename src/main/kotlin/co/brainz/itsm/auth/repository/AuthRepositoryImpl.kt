/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.QAliceAuthEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.auth.dto.AuthListDto
import co.brainz.itsm.auth.dto.AuthSearchCondition
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AuthRepositoryImpl : QuerydslRepositorySupport(
    AliceAuthEntity::class.java
), AuthRepositoryCustom {

    override fun findAuthSearch(authSearchCondition: AuthSearchCondition): PagingReturnDto {
        val auth = QAliceAuthEntity.aliceAuthEntity
        val query = from(auth)
            .select(
                Projections.constructor(
                    AuthListDto::class.java,
                    auth.authId,
                    auth.authName,
                    auth.authDesc
                )
            )
            .where(this.builder(authSearchCondition, auth))
            .orderBy(auth.authName.asc())
            if (authSearchCondition != null) {
                query.limit(authSearchCondition.contentNumPerPage)
                    .offset((authSearchCondition.pageNum - 1) * authSearchCondition.contentNumPerPage)
            }

        val countQuery = from(auth)
            .select(auth.count())
            .where(this.builder(authSearchCondition, auth))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    private fun builder(authSearchCondition: AuthSearchCondition, auth: QAliceAuthEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(auth.authName, authSearchCondition.searchValue)
            ?.or(super.likeIgnoreCase(auth.authDesc, authSearchCondition.searchValue)))
        return builder
    }
}
