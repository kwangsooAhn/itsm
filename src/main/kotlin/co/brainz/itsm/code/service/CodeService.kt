package co.brainz.itsm.code.service

import co.brainz.itsm.code.dto.CodeDetailDto
import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.mapper.CodeMapper
import co.brainz.itsm.code.repository.CodeRepository
import co.brainz.itsm.customCode.repository.CustomCodeRepository
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class CodeService(private val codeRepository: CodeRepository, private val customCodeRepository: CustomCodeRepository) {

    private val codeMapper: CodeMapper = Mappers.getMapper(CodeMapper::class.java)

    fun selectCodeByParent(code: String): MutableList<CodeEntity> {
        return codeRepository.findByPCode(codeRepository.findById(code).orElse(CodeEntity(code = code)))
    }

    /**
     * 코드 데이터 전체 목록 조회
     */
    fun getCodeList(): MutableList<CodeDto> {
        val codeDto = mutableListOf<CodeDto>()
        codeRepository.findAll().forEach {
            when (it.editable) {
                true -> codeDto.add(codeMapper.toCodeDto(it))
            }
        }

        return codeDto
    }

    /**
     * 코드 데이터 상세 정보 조회
     */
    fun getDetailCodes(code: String): CodeDetailDto {
        val codeDetailDto = codeRepository.findCodeDetail(code)
        codeDetailDto.enabled = !customCodeRepository.existsByPCode(codeDetailDto.code)
        return codeDetailDto
    }

    /**
     * 코드 데이터 저장, 수정
     */
    fun createCode(codeDetailDto: CodeDetailDto) {
        var pCode = codeDetailDto.pCode

        var codeEntity = CodeEntity (
            codeDetailDto.code,
            codeRepository.findById(codeDetailDto.pCode!!).orElse(CodeEntity(code = pCode!!)),
            codeDetailDto.codeValue,
            codeDetailDto.editable
        )
        codeRepository.save(codeEntity)
    }

    /**
     * 코드 데이터 삭제
     */
    fun deleteCode(code: String) {
        codeRepository.deleteById(code)
    }
}
