package co.brainz.itsm.code.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.code.dto.CodeDetailDto
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.entity.CodeEntity
import com.querydsl.core.QueryResults

interface CodeRepositoryCustom : AliceRepositoryCustom {

    fun findByCodeList(search: String, pCode: String): QueryResults<CodeEntity>

    fun findCodeByPCodeIn(pCodes: Set<String>, lang: String?): List<CodeDto>

    fun findByCodeAll(): QueryResults<CodeEntity>

    fun findCodeDetail(search: String): CodeDetailDto

    fun findCodeByCodeLang(pCodes: String, lang: String?): List<CodeDto>
}
