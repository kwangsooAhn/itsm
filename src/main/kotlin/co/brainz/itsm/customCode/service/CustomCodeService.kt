package co.brainz.itsm.customCode.service

import co.brainz.itsm.customCode.constants.CustomCodeConstants
import co.brainz.itsm.customCode.repository.CustomCodeRepository
import co.brainz.itsm.customCode.dto.CustomCodeDto
import co.brainz.itsm.customCode.entity.CustomCodeEntity
import co.brainz.itsm.customCode.mapper.CustomCodeMapper
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class CustomCodeService(private val customCodeRepository: CustomCodeRepository) {

    private val customCodeMapper: CustomCodeMapper = Mappers.getMapper(CustomCodeMapper::class.java)

    /**
     * 사용자 정의 코드 리스트 조회.
     *
     * @return MutableList<CustomCodeDto>
     */
    fun getCustomCodes(): MutableList<CustomCodeDto> {
        val customCodeEntityList = customCodeRepository.findAll()
        val customCodeList = mutableListOf<CustomCodeDto>()
        for (customCodeEntity in customCodeEntityList) {
            customCodeList.add(customCodeMapper.toCustomCodeDto(customCodeEntity))
            //TODO 폼디자이너에서 사용할 경우 enabled false 로 변경
        }
        return customCodeList
    }

    /**
     * 사용자 정의 코드 조회.
     *
     * @param customCodeId
     * @return CustomCodeDto
     */
    fun getCustomCode(customCodeId: String): CustomCodeDto {
        val customCodeEntity = customCodeRepository.findById(customCodeId).orElse(CustomCodeEntity())
        return customCodeMapper.toCustomCodeDto(customCodeEntity)
    }

    /**
     * 사용자 정의 코드 저장(등록/수정).
     *
     * @param customCodeDto
     * @return String
     */
    fun saveCustomCode(customCodeDto: CustomCodeDto): String {
        var code = customCodeEditValid(customCodeDto)
        when(code) {
            CustomCodeConstants.Status.STATUS_VALID_SUCCESS.code -> {
                customCodeRepository.save(customCodeMapper.toCustomCodeEntity(customCodeDto))
                code = CustomCodeConstants.Status.STATUS_SUCCESS.code
            }
        }
        return code
    }

    /**
     * 사용자 정의 코드 삭제.
     *
     * @param customCodeId
     */
    fun deleteCustomCode(customCodeId: String) {
        customCodeRepository.deleteById(customCodeId)
    }

    /**
     * 저장 가능한 데이터인지 확인.
     *
     * @param customCodeDto
     * @return String
     */
    fun customCodeEditValid(customCodeDto: CustomCodeDto): String {
        println("===========customCodeDto >\n$customCodeDto")
        //TODO: 수정일 경우 Check.
        return when {
            customCodeRepository.existsByCustomCodeName(customCodeDto.customCodeName) -> {
                CustomCodeConstants.Status.STATUS_ERROR_CUSTOM_CODE_NAME_DUPLICATION.code
            }
            customCodeRepository.countByTableName(customCodeDto.targetTable) == 0 -> {
                CustomCodeConstants.Status.STATUS_ERROR_TARGET_TABLE_NOT_EXIST.code
            }
            customCodeRepository.countByColumnName(customCodeDto.targetTable, customCodeDto.keyColumn) == 0 -> {
                CustomCodeConstants.Status.STATUS_ERROR_KEY_COLUMN_NOT_EXIST.code
            }
            customCodeRepository.countByColumnName(customCodeDto.targetTable, customCodeDto.valueColumn) == 0 -> {
                CustomCodeConstants.Status.STATUS_ERROR_VALUE_COLUMN_NOT_EXIST.code
            }
            else -> {
                CustomCodeConstants.Status.STATUS_VALID_SUCCESS.code
            }
        }
    }
}
