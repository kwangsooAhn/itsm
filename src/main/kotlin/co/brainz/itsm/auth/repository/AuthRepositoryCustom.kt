/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.auth.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.auth.dto.AuthListDto
import co.brainz.itsm.auth.dto.AuthSearchCondition
import com.querydsl.core.QueryResults
import org.springframework.data.domain.Page

interface AuthRepositoryCustom : AliceRepositoryCustom {
    fun findAuthSearch(authSearchCondition: AuthSearchCondition): Page<AuthListDto>
}
