package co.brainz.itsm.code.service

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.code.constants.CodeConstants
import co.brainz.itsm.code.dto.CodeDetailDto
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.repository.CodeRepository
import co.brainz.itsm.customCode.repository.CustomCodeRepository
import co.brainz.itsm.user.repository.UserRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service
class CodeService(
    private val codeRepository: CodeRepository,
    private val customCodeRepository: CustomCodeRepository,
    private val userRepository: UserRepository
) {

    fun selectCodeByParent(code: String): MutableList<CodeEntity> {
        return codeRepository.findByPCodeOrderByCode(codeRepository.findById(code).orElse(CodeEntity(code = code)))
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
     * 코드 데이터 전체 목록 조회
     */
    fun getCodeList(search: String, pCode: String): MutableList<CodeDto> {
        val queryResults = codeRepository.findByCodeList(search, pCode)
        var codeSearchList = queryResults.results
        val pCodeList = mutableListOf<CodeEntity>()
        if (search.isNotEmpty()) {
            for (code in queryResults.results) {
                var tempCode = code.pCode
                do {
                    if (tempCode != null) {
                        pCodeList.add(tempCode)
                        tempCode = tempCode.pCode
                    }
                } while (tempCode != null)
            }
        }
        if (pCodeList.isNotEmpty()) {
            codeSearchList.addAll(pCodeList)
            codeSearchList = codeSearchList.distinct()
        }
        val codeList = mutableListOf<CodeDto>()
        for (codeEntity in codeSearchList) {
            codeList.add(
                CodeDto(
                    code = codeEntity.code,
                    pCode = codeEntity.pCode?.code,
                    updateDt = codeEntity.updateDt,
                    createDt = codeEntity.createDt,
                    codeName = codeEntity.codeName,
                    codeValue = codeEntity.codeValue,
                    codeDesc = codeEntity.codeDesc,
                    editable = codeEntity.editable,
                    createUserName = codeEntity.createUser?.userName,
                    updateUserName = codeEntity.updateUser?.userName,
                    level = codeEntity.level,
                    totalCount = queryResults.total
                )
            )
        }
        return codeList
    }

    /**
     * 코드 데이터 상세 정보 조회
     */
    fun getDetailCodes(code: String): CodeDetailDto? {
        return try {
            val codeDetailDto = codeRepository.findCodeDetail(code)
            codeDetailDto.enabled = !customCodeRepository.existsByPCode(codeDetailDto.code)
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
            codeValue = codeDetailDto.codeValue,
            editable = codeDetailDto.editable
        )

        if (codeRepository.existsByCodeAndEditableTrue(codeDetailDto.code)) {
            status = CodeConstants.Status.STATUS_ERROR_CODE_DUPLICATION.code
        } else if (!codeRepository.existsByCodeAndEditableTrue(codeDetailDto.pCode!!) && codeDetailDto.pCode != "") {
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
            codeValue = codeDetailDto.codeValue,
            editable = codeDetailDto.editable
        )

        if (codeDetailDto.pCode.isNullOrEmpty()) {
            codeEntity.level = 1
        } else {
            val pCodeEntity = codeRepository.findCodeDetail(codeDetailDto.pCode!!)
            codeEntity.level = pCodeEntity.level!! + 1
        }

        when (codeRepository.existsByCodeAndEditableTrue(codeDetailDto.pCode!!) || codeDetailDto.pCode == "") {
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
}
