package co.brainz.itsm.code.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.code.dto.CodeLangDto

interface CodeLangRepositoryCustom : AliceRepositoryCustom {
    fun findByCodeLangList(search: String): MutableList<CodeLangDto>
}