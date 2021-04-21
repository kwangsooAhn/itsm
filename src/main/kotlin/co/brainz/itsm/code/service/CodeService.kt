package co.brainz.itsm.code.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.code.constants.CodeConstants
import co.brainz.itsm.code.dto.CodeDetailDto
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.dto.CodeReturnDto
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.repository.CodeRepository
import co.brainz.itsm.user.repository.UserRepository
import com.querydsl.core.QueryResults
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CodeService(
    private val codeRepository: CodeRepository,
    private val userRepository: UserRepository
) {

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
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val findCodeList = codeRepository.findCodeByPCodeIn(codes, userDetails.lang)
        for (code in findCodeList) {
            if (code.codeLangValue != null && code.lang != null) {
                code.codeValue = code.codeLangValue
            }
        }
        codeList.addAll(findCodeList)
        return codeList
    }

    /**
     * 커스텀 코드 관련 코드 리스트 조회.
     */
    fun getCodeListByCustomCode(code: String): MutableList<CodeEntity> {
        return codeRepository.findByPCodeAndEditableTrueOrderByCodeValue(
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
        return try {
            val codeDetailDto = codeRepository.findCodeDetail(code)
            codeDetailDto.createUserName =
                codeDetailDto.createUserName?.let { userRepository.findById(it).orElse(AliceUserEntity()).userName }
            codeDetailDto.updateUserName =
                codeDetailDto.updateUserName?.let { userRepository.findById(it).orElse(AliceUserEntity()).userName }
            codeDetailDto
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    /**
     * 코드 데이터 저장
     */
    fun createCode(codeDetailDto: CodeDetailDto): String {
        var status = CodeConstants.Status.STATUS_SUCCESS.code
        val codeEntity = CodeEntity(
            code = codeDetailDto.code,
            pCode = codeRepository.findById(codeDetailDto.pCode!!).orElse(CodeEntity(code = codeDetailDto.pCode!!)),
            codeName = codeDetailDto.codeName,
            codeValue = codeDetailDto.codeValue,
            codeDesc = codeDetailDto.codeDesc,
            editable = codeDetailDto.editable,
            seqNum = codeDetailDto.seqNum
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
        return status
    }

    /**
     * 코드 데이터 수정
     */
    fun updateCode(codeDetailDto: CodeDetailDto): String {
        var status = CodeConstants.Status.STATUS_SUCCESS_EDIT_CODE.code
        val codeEntity = CodeEntity(
            code = codeDetailDto.code,
            pCode = codeRepository.findById(codeDetailDto.pCode!!).orElse(CodeEntity(code = codeDetailDto.pCode!!)),
            codeName = codeDetailDto.codeName,
            codeValue = codeDetailDto.codeValue,
            codeDesc = codeDetailDto.codeDesc,
            editable = codeDetailDto.editable,
            seqNum = codeDetailDto.seqNum
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
        return status
    }

    /**
     * 코드 데이터 삭제
     */
    fun deleteCode(code: String): String {
        var status = CodeConstants.Status.STATUS_SUCCESS.code

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
