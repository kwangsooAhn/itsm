/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.code.repository

import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.entity.QCodeEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CodeRepositoryImpl : QuerydslRepositorySupport(CodeEntity::class.java),
    CodeRepositoryCustom {

    override fun findByCodeAll(): QueryResults<CodeEntity> {
        val code = QCodeEntity.codeEntity
        return from(code)
            .orderBy(code.level.asc(), code.code.asc())
            .fetchResults()
    }

    override fun countByCodeList(search: String, pCode: String): Long {
        val code = QCodeEntity.codeEntity
        return from(code)
            .select(code)
            .where(
                super.like(
                    code.code, search
                )
            )
            .where(
                super.eq(
                    code.pCode.code, pCode
                )
            )
            .fetchCount()
    }

    override fun findByCodeList(search: String, pCode: String): QueryResults<CodeEntity> {
        val code = QCodeEntity.codeEntity
        return from(code)
            .select(code)
            .where(
                super.like(
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

    override fun findCodeByPCodeIn(pCodes: Set<String>): List<CodeDto> {
        val code = QCodeEntity.codeEntity

        return from(code)
            .select(
                Projections.constructor(
                    CodeDto::class.java,
                    code.code,
                    code.pCode.code,
                    code.codeValue,
                    code.codeName,
                    code.codeDesc,
                    code.editable,
                    code.createDt,
                    code.createUser.userName,
                    code.level,
                    code.seqNum,
                    Expressions.numberPath(Long::class.java, "0")
                )
            )
            .where(code.pCode.code.`in`(pCodes))
            .orderBy(code.seqNum.asc(), code.code.asc())
            .fetch()
    }
}
