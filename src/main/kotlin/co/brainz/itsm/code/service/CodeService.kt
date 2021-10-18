/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.code.service

import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.code.constants.CodeConstants
import co.brainz.itsm.code.dto.CodeDetailDto
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.dto.CodeReturnDto
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.entity.CodeLangEntity
import co.brainz.itsm.code.entity.CodeLangEntityPk
import co.brainz.itsm.code.repository.CodeLangRepository
import co.brainz.itsm.code.repository.CodeRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.querydsl.core.QueryResults
import javax.transaction.Transactional
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CodeService(
    private val codeRepository: CodeRepository,
    private val codeLangRepository: CodeLangRepository,
    private val currentSessionUser: CurrentSessionUser
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun selectCodeByParent(code: Any): MutableList<CodeDto> {
        val codeList = mutableListOf<CodeDto>()
        val codes = mutableSetOf<String>()
        when (code) {
            is String -> {
                codes.add(code)
            }
            is MutableList<*> -> {
                codes.addAll(code as Collection<String>)
            }
            is Set<*> -> {
                codes.addAll(code as Set<String>)
            }
        }
        val lang = if (SecurityContextHolder.getContext().authentication.principal == "anonymousUser") {
            AliceUserConstants.USER_LOCALE_LANG
        } else {
            currentSessionUser.getUserDto()?.lang
        }
        val findCodeList = codeRepository.findCodeByPCodeIn(codes, lang)
        for (codeDto in findCodeList) {
            if (codeDto.codeLangName != null && codeDto.lang != null) {
                codeDto.codeName = codeDto.codeLangName
            }
        }
        codeList.addAll(findCodeList)
        return codeList
    }

    /**
     * 커스텀 코드 관련 코드 리스트 조회.
     */
    fun getCodeListByCustomCode(code: String): MutableList<CodeEntity> {
        return codeRepository.findByPCodeOrderByCodeValue(
            codeRepository.findById(code).orElse(CodeEntity(code = code))
        )
    }

    /**
     * 트리 조회.
     */
    fun getCodeList(search: String, pCode: String): CodeReturnDto {
        val treeCodeList = mutableListOf<CodeDto>()
        val queryResults: QueryResults<CodeEntity>
        var returnList = emptyList<CodeEntity>()
        var count = 0L
        when (search.isEmpty()) {
            true -> {
                queryResults = codeRepository.findByCodeAll()
                val allList = queryResults.results
                val codeSearchList = mutableListOf<CodeEntity>()
                if (pCode.isNotEmpty()) {
                    val codeList = mutableListOf<CodeEntity>()
                    val pCodeEntity = allList.filter { it.code == pCode }[0]
                    if (pCodeEntity !== null) {
                        codeList.add(pCodeEntity)
                        getChildCode(allList, pCodeEntity, codeList)
                    }
                    codeSearchList.addAll(codeList)
                } else {
                    codeSearchList.addAll(allList)
                }
                count = codeSearchList.size.toLong()
                returnList = codeSearchList
            }
            false -> {
                queryResults = codeRepository.findByCodeList(search, pCode)
                var codeSearchList = queryResults.results
                val pCodeList = mutableListOf<CodeEntity>()
                for (code in queryResults.results) {
                    var tempCode = code.pCode
                    do {
                        if (tempCode != null) {
                            pCodeList.add(tempCode)
                            tempCode = tempCode.pCode
                        }
                    } while (tempCode != null)
                }
                if (pCodeList.isNotEmpty()) {
                    codeSearchList.addAll(pCodeList)
                    codeSearchList = codeSearchList.distinct()
                }
                count = queryResults.total
                returnList = codeSearchList
            }
        }
        for (codeEntity in returnList) {
            treeCodeList.add(
                CodeDto(
                    code = codeEntity.code,
                    pCode = codeEntity.pCode?.code,
                    createDt = codeEntity.createDt,
                    codeName = codeEntity.codeName,
                    codeValue = codeEntity.codeValue,
                    codeDesc = codeEntity.codeDesc,
                    editable = codeEntity.editable,
                    level = codeEntity.level,
                    seqNum = codeEntity.seqNum
                )
            )
        }
        return CodeReturnDto(
            data = treeCodeList,
            totalCount = count
        )
    }

    /**
     * 코드 데이터 상세 정보 조회
     */
    fun getDetailCodes(code: String): CodeDetailDto? {
        val codeDetailDto = codeRepository.findCodeDetail(code)
        val codeLangList = codeLangRepository.findByCodeLangList(code)

        if (codeLangList.isNotEmpty()) {
            codeDetailDto.codeLang = mapper.writeValueAsString(codeLangList)
        }

        return try {
            codeDetailDto
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * 코드 데이터 저장
     */
    @Transactional
    fun createCode(codeDetailDto: CodeDetailDto): String {
        var status = CodeConstants.Status.STATUS_SUCCESS.code
        val codeEntity = CodeEntity(
            code = codeDetailDto.code,
            pCode = codeRepository.findById(codeDetailDto.pCode!!).orElse(CodeEntity(code = codeDetailDto.pCode!!)),
            codeName = codeDetailDto.codeName,
            codeValue = codeDetailDto.codeValue,
            codeDesc = codeDetailDto.codeDesc,
            editable = codeDetailDto.editable,
            seqNum = codeDetailDto.seqNum,
            useYn = codeDetailDto.useYn
        )

        if (codeRepository.existsByCodeAndEditableTrue(codeDetailDto.code)) {
            status = CodeConstants.Status.STATUS_ERROR_CODE_DUPLICATION.code
        } else if (!codeRepository.existsByCode(codeDetailDto.pCode!!) && codeDetailDto.pCode != "") {
            status = CodeConstants.Status.STATUS_ERROR_CODE_P_CODE_NOT_EXIST.code
        } else {
            if (!codeDetailDto.pCode.isNullOrEmpty()) {
                val pCodeEntity = codeRepository.findCodeDetail(codeDetailDto.pCode!!)
                codeEntity.level = pCodeEntity.level!! + 1
            } else {
                codeEntity.level = 1
            }
            codeRepository.save(codeEntity)
        }

        if (!codeDetailDto.codeLang.isNullOrEmpty()) {
            val codeLangObject: LinkedHashMap<String, String> =
                mapper.readValue(codeDetailDto.codeLang, object : TypeReference<Map<String, String>>() {})
            codeLangObject.entries.forEach {
                codeLangRepository.save(
                    CodeLangEntity(
                        code = codeDetailDto.code,
                        codeName = it.value,
                        lang = it.key
                    )
                )
            }
        }

        return status
    }

    /**
     * 코드 데이터 수정
     */
    @Transactional
    fun updateCode(codeDetailDto: CodeDetailDto): String {
        var status = CodeConstants.Status.STATUS_SUCCESS_EDIT_CODE.code
        val codeEntity = CodeEntity(
            code = codeDetailDto.code,
            pCode = codeRepository.findById(codeDetailDto.pCode!!).orElse(CodeEntity(code = codeDetailDto.pCode!!)),
            codeName = codeDetailDto.codeName,
            codeValue = codeDetailDto.codeValue,
            codeDesc = codeDetailDto.codeDesc,
            editable = codeDetailDto.editable,
            seqNum = codeDetailDto.seqNum,
            useYn = codeDetailDto.useYn
        )

        if (codeDetailDto.pCode.isNullOrEmpty()) {
            codeEntity.level = 0
        } else {
            val pCodeEntity = codeRepository.findCodeDetail(codeDetailDto.pCode!!)
            codeEntity.level = pCodeEntity.level!! + 1
        }

        when (codeRepository.existsByCode(codeDetailDto.pCode!!) || codeDetailDto.pCode == "") {
            true -> {
                codeRepository.save(codeEntity)
            }
            false -> {
                status = CodeConstants.Status.STATUS_ERROR_CODE_P_CODE_NOT_EXIST.code
            }
        }

        val codeLangList = codeLangRepository.findByCodeLangList(codeDetailDto.code)
        if (codeLangList.isNotEmpty()) {
            for (codeLangDto in codeLangList) {
                codeLangRepository.deleteById(
                    CodeLangEntityPk(codeLangDto.code, codeLangDto.lang)
                )
            }
        }

        if (!codeDetailDto.codeLang.isNullOrEmpty()) {
            val codeLangObject: LinkedHashMap<String, String> =
                mapper.readValue(codeDetailDto.codeLang, object : TypeReference<Map<String, String>>() {})
            codeLangObject.entries.forEach {
                codeLangRepository.save(
                    CodeLangEntity(
                        code = codeDetailDto.code,
                        codeName = it.value,
                        lang = it.key
                    )
                )
            }
        }

        return status
    }

    /**
     * 코드 데이터 삭제
     */
    @Transactional
    fun deleteCode(code: String): String {
        var status = CodeConstants.Status.STATUS_SUCCESS.code
        val codeLangList = codeLangRepository.findByCodeLangList(code)

        if (codeLangList.isNotEmpty()) {
            for (codeLangDto in codeLangList) {
                codeLangRepository.deleteById(
                    CodeLangEntityPk(codeLangDto.code, codeLangDto.lang)
                )
            }
        }

        when (codeRepository.existsByPCodeAndEditableTrue(
            codeRepository.findById(code).orElse(CodeEntity(code = code))
        )) {
            true -> {
                status = CodeConstants.Status.STATUS_ERROR_CODE_P_CODE_USED.code
            }
            false -> {
                codeRepository.deleteById(code)
            }
        }
        return status
    }

    /**
     * 부모코드로 자식코드를 찾는다.
     */
    private fun getChildCode(
        allList: MutableList<CodeEntity>,
        pCodeEntity: CodeEntity,
        codeList: MutableList<CodeEntity>
    ) {
        val childList = allList.filter { it.pCode?.code == pCodeEntity.code }
        for (child in childList) {
            codeList.add(child)
            getChildCode(allList, child, codeList)
        }
    }
}
