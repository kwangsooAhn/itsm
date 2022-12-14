/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.auth.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.auth.dto.AuthSearchCondition

interface AuthRepositoryCustom : AliceRepositoryCustom {
    fun findAuthSearch(authSearchCondition: AuthSearchCondition): PagingReturnDto
}
