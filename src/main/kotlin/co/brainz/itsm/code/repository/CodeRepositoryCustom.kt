package co.brainz.itsm.code.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.code.entity.CodeEntity
import com.querydsl.core.QueryResults

interface CodeRepositoryCustom : AliceRepositoryCustom {

    fun findByCodeList(search: String): QueryResults<CodeEntity>
}
