package co.brainz.itsm.code.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.entity.CodeEntity
import com.querydsl.core.QueryResults

interface CodeRepositoryCustom : AliceRepositoryCustom {

    fun findByCodeList(search: String, pCode: String): QueryResults<CodeEntity>

    fun findCodeByPCodeIn(pCodes: Set<String>, lang: String?): MutableList<CodeDto>

    fun findByCodeAll(): QueryResults<CodeEntity>

    fun countByCodeList(search: String, pCode: String): Long
}
