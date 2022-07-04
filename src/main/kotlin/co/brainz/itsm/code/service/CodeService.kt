/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.code.service

import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.CurrentSessionUser
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
import javax.transaction.Transactional
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CodeService(
    private val aliceMessageSource: AliceMessageSource,
    private val codeRepository: CodeRepository,
    private val codeLangRepository: CodeLangRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val excelComponent: ExcelComponent
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
        return codeRepository.findByPCodeOrderBySeqNum(
            codeRepository.findById(code).orElse(CodeEntity(code = code))
        )
    }

    /**
     * 트리 조회.
     */
    fun getCodeList(search: String, pCode: String): CodeReturnDto {
        val treeCodeList = mutableListOf<CodeDto>()
        val pagingResult: List<CodeEntity>
        var returnList = emptyList<CodeEntity>()
        var count = 0L
        when (search.isEmpty()) {
            true -> {
                pagingResult = codeRepository.findByCodeAll()
                val codeSearchList = mutableListOf<CodeEntity>()
                if (pCode.isNotEmpty()) {
                    val codeList = mutableListOf<CodeEntity>()
                    val pCodeEntity = pagingResult.filter { it.code == pCode }[0]
                    if (pCodeEntity !== null) {
                        codeList.add(pCodeEntity)
                        getChildCode(pagingResult, pCodeEntity, codeList)
                    }
                    codeSearchList.addAll(codeList)
                } else {
                    codeSearchList.addAll(pagingResult)
                }
                count = codeSearchList.size.toLong()
                returnList = codeSearchList
            }
            false -> {
                pagingResult = codeRepository.findByCodeList(search, pCode)
                var codeSearchList = pagingResult
                val pCodeList = mutableListOf<CodeEntity>()
                for (code in pagingResult) {
                    var tempCode = code.pCode
                    do {
                        if (tempCode != null) {
                            pCodeList.add(tempCode)
                            tempCode = tempCode.pCode
                        }
                    } while (tempCode != null)
                }
                if (pCodeList.isNotEmpty()) {
                    codeSearchList += pCodeList
                    codeSearchList = codeSearchList.distinct()
                }
                count = pagingResult.size.toLong()
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
    fun createCode(codeDetailDto: CodeDetailDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
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

        if (codeRepository.existsByCode(codeDetailDto.code)) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        } else if (!codeRepository.existsByCode(codeDetailDto.pCode!!) && codeDetailDto.pCode != "") {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
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

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 코드 데이터 수정
     */
    @Transactional
    fun updateCode(codeDetailDto: CodeDetailDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
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
                status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
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

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 코드 데이터 삭제
     */
    @Transactional
    fun deleteCode(code: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
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
            true -> status = ZResponseConstants.STATUS.ERROR_EXIST
            false -> codeRepository.deleteById(code)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 부모코드로 자식코드를 찾는다.
     */
    private fun getChildCode(
        allList: List<CodeEntity>,
        pCodeEntity: CodeEntity,
        codeList: MutableList<CodeEntity>
    ) {
        val childList = allList.filter { it.pCode?.code == pCodeEntity.code }
        for (child in childList) {
            codeList.add(child)
            getChildCode(allList, child, codeList)
        }
    }

    /**
     * 트리 조회 Excel 다운로드.
     */
    fun getCodeListExcelDownload(search: String, pCode: String): ResponseEntity<ByteArray> {
        val results = this.getCodeList(search, pCode)
        val excelVO = ExcelVO(
            sheets = mutableListOf(
                ExcelSheetVO(
                    rows = mutableListOf(
                        ExcelRowVO(
                            cells = listOf(
                                ExcelCellVO(value = aliceMessageSource.getMessage("code.label.code"), cellWidth = 5000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("code.label.pCode"), cellWidth = 5000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("code.label.name"), cellWidth = 3000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("code.label.codeValue"), cellWidth = 6000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("code.label.seqNum")),
                                ExcelCellVO(value = aliceMessageSource.getMessage("code.label.description"), cellWidth = 10000)
                            )
                        )
                    )
                )
            )
        )
        results.data.forEach { result ->
            excelVO.sheets[0].rows.add(
                ExcelRowVO(
                    cells = mutableListOf(
                        ExcelCellVO(value = result.code),
                        ExcelCellVO(value = result.pCode),
                        ExcelCellVO(value = result.codeName ?: ""),
                        ExcelCellVO(value = result.codeValue ?: ""),
                        ExcelCellVO(value = result.seqNum ?: ""),
                        ExcelCellVO(value = result.codeDesc ?: "")
                    )
                )
            )
        }
        return excelComponent.download(excelVO)
    }
}
