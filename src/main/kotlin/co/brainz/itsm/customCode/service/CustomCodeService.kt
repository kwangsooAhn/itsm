package co.brainz.itsm.customCode.service

import co.brainz.itsm.customCode.constants.CustomCodeConstants
import co.brainz.itsm.customCode.dto.CustomCodeColumnDto
import co.brainz.itsm.customCode.repository.CustomCodeRepository
import co.brainz.itsm.customCode.dto.CustomCodeDto
import co.brainz.itsm.customCode.dto.CustomCodeTableDto
import co.brainz.itsm.customCode.entity.CustomCodeColumnEntity
import co.brainz.itsm.customCode.entity.CustomCodeColumnPk
import co.brainz.itsm.customCode.entity.CustomCodeEntity
import co.brainz.itsm.customCode.mapper.CustomCodeColumnMapper
import co.brainz.itsm.customCode.mapper.CustomCodeMapper
import co.brainz.itsm.customCode.mapper.CustomCodeTableMapper
import co.brainz.itsm.customCode.repository.CustomCodeColumnRepository
import co.brainz.itsm.customCode.repository.CustomCodeTableRepository
import co.brainz.itsm.form.service.FormService
import com.fasterxml.jackson.databind.ObjectMapper
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class CustomCodeService(private val customCodeRepository: CustomCodeRepository,
                        private val customCodeTableRepository: CustomCodeTableRepository,
                        private val customCodeColumnRepository: CustomCodeColumnRepository,
                        private val formService: FormService) {

    private val customCodeMapper: CustomCodeMapper = Mappers.getMapper(CustomCodeMapper::class.java)
    private val customCodeTableMapper: CustomCodeTableMapper = Mappers.getMapper(CustomCodeTableMapper::class.java)
    private val customCodeColumnMapper: CustomCodeColumnMapper = Mappers.getMapper(CustomCodeColumnMapper::class.java)

    /**
     * 사용자 정의 코드 리스트 조회.
     *
     * @return MutableList<CustomCodeDto>
     */
    fun getCustomCodeList(): List<CustomCodeDto> {
        val customCodeEntityList = customCodeRepository.findByOrderByCustomCodeNameDesc()
        val customCodeList = mutableListOf<CustomCodeDto>()

        val customCodeTableNameList = getCustomCodeTableNameList()
        val usedCustomCodeIdList = getUsedCustomCodeIdList()

        for (customCodeEntity in customCodeEntityList) {
            val customCode = customCodeMapper.toCustomCodeDto(customCodeEntity)
            customCodeTableNameList[customCodeEntity.targetTable]?.let { customCode.targetTableName = it }
            customCode.enabled = (usedCustomCodeIdList.indexOf(customCodeEntity.customCodeId) == -1)
            customCodeList.add(customCode)
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
        val customCodeDto = customCodeMapper.toCustomCodeDto(customCodeEntity)
        customCodeDto.targetTableName = customCodeTableRepository.findByCustomCodeTable(customCodeDto.targetTable).customCodeTableName
        customCodeDto.searchColumnName = getCustomCodeColumnName(customCodeDto.targetTable, customCodeDto.searchColumn, CustomCodeConstants.ColumnType.SEARCH.code)
        customCodeDto.valueColumnName = getCustomCodeColumnName(customCodeDto.targetTable, customCodeDto.valueColumn, CustomCodeConstants.ColumnType.VALUE.code)
        return customCodeDto
    }

    /**
     * 사용자 정의 코드 저장(등록/수정).
     *
     * @param customCodeDto
     * @return String
     */
    fun saveCustomCode(customCodeDto: CustomCodeDto): String {
        var code = customCodeEditValid(customCodeDto)
        when (code) {
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
     * @return String
     */
    fun deleteCustomCode(customCodeId: String): String {
        return if (getUsedCustomCodeIdList().indexOf(customCodeId) != -1) {
            CustomCodeConstants.Status.STATUS_ERROR_CUSTOM_CODE_USED.code
        } else {
            customCodeRepository.deleteById(customCodeId)
            CustomCodeConstants.Status.STATUS_SUCCESS.code
        }
    }

    /**
     * 사용중인 사용자 정의 코드 ID 리스트 조회.
     *
     * @return List<String>
     */
    fun getUsedCustomCodeIdList(): List<String> {
        val usedCustomCodeIdList = mutableListOf<String>()
        val formComponentDataList = formService.getFormComponentDataList(CustomCodeConstants.COMPONENT_TYPE_CUSTOM_CODE)
        for (formComponentData in formComponentDataList) {
            if (formComponentData.attributeId == CustomCodeConstants.ATTRIBUTE_ID_DISPLAY) {
                val map = ObjectMapper().readValue(formComponentData.attributeValue, MutableMap::class.java)
                map[CustomCodeConstants.COMPONENT_TYPE_CUSTOM_CODE]?.let { usedCustomCodeIdList.add(it.toString()) }
            }
        }
        return usedCustomCodeIdList
    }

    /**
     * 사용자 정의 코드 테이블 리스트 조회.
     *
     * @return MutableList<CustomCodeTableDto>
     */
    fun getCustomCodeTableList(): MutableList<CustomCodeTableDto> {
        val customCodeTableEntityList = customCodeTableRepository.findAll()
        val customCodeTableList = mutableListOf<CustomCodeTableDto>()
        for (customCodeTableEntity in customCodeTableEntityList) {
            customCodeTableList.add(customCodeTableMapper.toCustomCodeTableDto(customCodeTableEntity))
        }
        return customCodeTableList
    }

    /**
     * 사용자 정의 코드 테이블 이름 리스트 조회.
     *
     * @return List<String>
     */
    fun getCustomCodeTableNameList(): Map<String, String> {
        val customCodeTableNameList = mutableMapOf<String, String>()
        val customCodeTableList = customCodeTableRepository.findAll()
        for (customCodeTable in customCodeTableList) {
            customCodeTableNameList[customCodeTable.customCodeTable] = customCodeTable.customCodeTableName
        }
        return customCodeTableNameList
    }

    /**
     * 사용자 정의 코드 컬럼 리스트 조회.
     *
     * @return MutableList<CustomCodeColumnDto>
     */
    fun getCustomCodeColumnList(): MutableList<CustomCodeColumnDto> {
        val customCodeColumnEntityList = customCodeColumnRepository.findAll()
        val customCodeColumnList = mutableListOf<CustomCodeColumnDto>()
        for (customCodeColumnEntity in customCodeColumnEntityList) {
            customCodeColumnList.add(customCodeColumnMapper.toCustomCodeColumnDto(customCodeColumnEntity))
        }
        return customCodeColumnList
    }

    /**
     * 사용자 정의 코드 컬럼 이름 조회.
     *
     * @return String
     */
    fun getCustomCodeColumnName(customCodeTable: String, customCodeColumn: String, customCodeType: String): String {
        return customCodeColumnRepository.findById(CustomCodeColumnPk(
                customCodeTable = customCodeTable,
                customCodeColumn = customCodeColumn,
                customCodeType = customCodeType
        )).orElse(CustomCodeColumnEntity()).customCodeColumnName
    }

    /**
     * 저장 가능한 데이터인지 확인.
     *
     * @param customCodeDto
     * @return String
     */
    fun customCodeEditValid(customCodeDto: CustomCodeDto): String {
        var isContinue = true
        var code = CustomCodeConstants.Status.STATUS_VALID_SUCCESS.code
        if (customCodeDto.customCodeId != "") {
            if (getUsedCustomCodeIdList().indexOf(customCodeDto.customCodeId) != -1) {
                code = CustomCodeConstants.Status.STATUS_ERROR_CUSTOM_CODE_USED.code
                isContinue = false
            }
            if (isContinue) {
                val existCustomCode = customCodeRepository.findById(customCodeDto.customCodeId).orElse(CustomCodeEntity())
                isContinue = (customCodeDto.customCodeName != existCustomCode.customCodeName)
            }
        }
        if (isContinue && customCodeRepository.existsByCustomCodeName(customCodeDto.customCodeName)) {
            code = CustomCodeConstants.Status.STATUS_ERROR_CUSTOM_CODE_NAME_DUPLICATION.code
        }
        return code
    }
}