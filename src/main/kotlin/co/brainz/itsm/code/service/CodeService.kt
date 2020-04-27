package co.brainz.itsm.code.service

import co.brainz.itsm.code.dto.CodeDto
import co.brainz.itsm.code.dto.CodeDtoHeechan
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.mapper.CodeMapper
import co.brainz.itsm.code.repository.CodeRepository
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class CodeService(private val codeRepository: CodeRepository) {

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
    fun getDetailCodes(code: String): CodeDtoHeechan {
        // return codeMapper.toCodeDto(codeRepository.findById(code).orElse(CodeEntity()))
        return codeRepository.findTest(code)
    }

    /**
     * 코드 데이터 저장, 수정
     */
    fun saveCode(codeDto: CodeDto) {
        codeRepository.save(codeMapper.toCodeEntity(codeDto))
    }

    /**
     * 코드 데이터 삭제
     */
    fun deleteCode(code: String) {
        codeRepository.deleteById(code)
    }
}
