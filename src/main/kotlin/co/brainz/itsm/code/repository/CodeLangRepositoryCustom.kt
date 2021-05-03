/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.code.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.code.dto.CodeLangDto

interface CodeLangRepositoryCustom : AliceRepositoryCustom {
    fun findByCodeLangList(search: String): MutableList<CodeLangDto>
}
