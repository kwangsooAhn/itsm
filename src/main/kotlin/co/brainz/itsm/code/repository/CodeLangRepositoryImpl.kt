/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.code.repository

import co.brainz.itsm.code.dto.CodeLangDto
import co.brainz.itsm.code.entity.CodeLangEntity
import co.brainz.itsm.code.entity.QCodeLangEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CodeLangRepositoryImpl : QuerydslRepositorySupport(CodeLangEntity::class.java), CodeLangRepositoryCustom {
    override fun findByCodeLangList(search: String): MutableList<CodeLangDto> {
        val codeLang = QCodeLangEntity.codeLangEntity
        return from(codeLang)
            .select(
                Projections.constructor(
                    CodeLangDto::class.java,
                    codeLang.code,
                    codeLang.codeValue,
                    codeLang.lang
                )
            )
            .where(codeLang.code.eq(search))
            .fetch()
    }
}
