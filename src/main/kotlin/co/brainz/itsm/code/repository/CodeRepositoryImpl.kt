/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.code.repository

import co.brainz.cmdb.dto.SearchDto
import co.brainz.itsm.code.dto.CodeDetailDto
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.dto.PCodeDto
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.code.entity.QCodeLangEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CodeRepositoryImpl : QuerydslRepositorySupport(CodeEntity::class.java),
    CodeRepositoryCustom {

    override fun findByCodeAll(): QueryResults<CodeEntity> {
        val code = QCodeEntity.codeEntity
        return from(code)
            .orderBy(code.level.asc(), code.seqNum.asc())
            .fetchResults()
    }

    override fun findByCodeList(search: String, pCode: String): QueryResults<CodeEntity> {
        val code = QCodeEntity.codeEntity

        val builder = BooleanBuilder()
        builder.or(super.likeIgnoreCase(code.code, search))
        builder.or(super.likeIgnoreCase(code.codeName, search))

        return from(code)
            .select(code)
            .where(builder)
            .where(
                super.eq(
                    code.pCode.code, pCode
                )
            )
            .orderBy(code.level.asc(), code.code.asc())
            .fetchResults()
    }

    override fun findCodeByPCodeIn(pCodes: Set<String>, lang: String?): List<CodeDto> {
        val code = QCodeEntity.codeEntity
        val codeLang = QCodeLangEntity.codeLangEntity
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
                    code.level,
                    code.seqNum,
                    codeLang.codeName,
                    codeLang.lang
                )
            )
            .leftJoin(codeLang).on(code.code.eq(codeLang.code), codeLang.lang.eq(lang))
            .where(code.pCode.code.`in`(pCodes).and(code.useYn.eq(true)))
            .orderBy(code.seqNum.asc(), code.code.asc())
            .fetch()
    }

    override fun findCodeDetail(search: String): CodeDetailDto {
        val code = QCodeEntity.codeEntity
        return from(code)
            .select(
                Projections.constructor(
                    CodeDetailDto::class.java,
                    code.code,
                    code.pCode.code,
                    code.codeName,
                    code.codeValue,
                    code.codeDesc,
                    code.editable,
                    code.useYn,
                    code.level,
                    code.seqNum,
                    Expressions.asString("")
                )
            )
            .where(code.code.eq(search))
            .fetchOne()
    }

    override fun findCodeByCodeLang(pCodes: Set<String>, lang: String?): List<CodeDto> {
        val code = QCodeEntity.codeEntity
        val codeLang = QCodeLangEntity.codeLangEntity
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
                    code.level,
                    code.seqNum,
                    codeLang.codeName,
                    codeLang.lang
                )
            )
            .leftJoin(codeLang).on(code.code.eq(codeLang.code), codeLang.lang.eq(lang))
            .where(code.code.`in`(pCodes).and(code.useYn.eq(true)))
            .fetch()
    }

    override fun findCodeList(searchDto: SearchDto): QueryResults<PCodeDto> {
        val code = QCodeEntity.codeEntity
        val query = from(code)
            .select(
                Projections.constructor(
                    PCodeDto::class.java,
                    code.code,
                    code.codeValue,
                    code.codeName,
                    code.pCode.code
                )
            )
            .rightJoin(code.pCode, code).on(code.pCode.code.eq(code.code))
            .where(
                super.likeIgnoreCase(code.codeName, searchDto.search)
                    ?.or(super.likeIgnoreCase(code.codeValue, searchDto.search))
            )
        if (searchDto.limit != null) {
            query.limit(searchDto.limit)
        }
        if (searchDto.offset != null) {
            query.offset(searchDto.offset)
        }
        return query.fetchResults()
    }
}
