package co.brainz.itsm.code.repository

import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.entity.QCodeEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CodeRepositoryImpl : QuerydslRepositorySupport(CodeEntity::class.java),
    CodeRepositoryCustom {

    override fun findByCodeList(search: String, pCode: String): QueryResults<CodeEntity> {
        val code = QCodeEntity.codeEntity
        return from(code)
            .select(code)
            .where(
                super.likeIgnoreCase(
                    code.code, search
                )
            )
            .where(
                super.eq(
                    code.pCode.code, pCode
                )
            )
            .orderBy(code.level.asc(), code.code.asc())
            .fetchResults()
    }
}
