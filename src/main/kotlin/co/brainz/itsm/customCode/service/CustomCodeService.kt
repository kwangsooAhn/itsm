/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.repository.CodeRepository
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.component.service.ComponentService
import co.brainz.itsm.customCode.constants.CustomCodeConstants
import co.brainz.itsm.customCode.dto.CustomCodeColumnDto
import co.brainz.itsm.customCode.dto.CustomCodeConditionDto
import co.brainz.itsm.customCode.dto.CustomCodeCoreDto
import co.brainz.itsm.customCode.dto.CustomCodeDataDto
import co.brainz.itsm.customCode.dto.CustomCodeDto
import co.brainz.itsm.customCode.dto.CustomCodeListReturnDto
import co.brainz.itsm.customCode.dto.CustomCodeSearchCondition
import co.brainz.itsm.customCode.dto.CustomCodeTableDto
import co.brainz.itsm.customCode.entity.CustomCodeColumnEntity
import co.brainz.itsm.customCode.entity.CustomCodeColumnPk
import co.brainz.itsm.customCode.entity.CustomCodeEntity
import co.brainz.itsm.customCode.mapper.CustomCodeColumnMapper
import co.brainz.itsm.customCode.mapper.CustomCodeMapper
import co.brainz.itsm.customCode.mapper.CustomCodeTableMapper
import co.brainz.itsm.customCode.repository.CustomCodeColumnRepository
import co.brainz.itsm.customCode.repository.CustomCodeRepository
import co.brainz.itsm.customCode.repository.CustomCodeTableRepository
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.role.specification.RoleCustomCodeSpecification
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.itsm.user.specification.UserCustomCodeSpecification
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.persistence.Column
import kotlin.math.ceil
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomCodeService(
    private val customCodeRepository: CustomCodeRepository,
    private val customCodeTableRepository: CustomCodeTableRepository,
    private val customCodeColumnRepository: CustomCodeColumnRepository,
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val componentService: ComponentService,
    private val codeRepository: CodeRepository,
    private val codeService: CodeService
) {

    private val customCodeMapper: CustomCodeMapper = Mappers.getMapper(CustomCodeMapper::class.java)
    private val customCodeTableMapper: CustomCodeTableMapper = Mappers.getMapper(CustomCodeTableMapper::class.java)
    private val customCodeColumnMapper: CustomCodeColumnMapper = Mappers.getMapper(CustomCodeColumnMapper::class.java)

    /**
     * 사용자 정의 코드 리스트 조회.
     *
     * @return MutableList<CustomCodeDto>
     */
    fun getCustomCodeList(customCodeSearchCondition: CustomCodeSearchCondition): CustomCodeListReturnDto {
        val queryResult = customCodeRepository.findByCustomCodeList(customCodeSearchCondition)
        return CustomCodeListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = customCodeRepository.count(),
                currentPageNum = customCodeSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * 사용자 정의 코드 조회.
     *
     * @param customCodeId
     * @return CustomCodeDto
     */
    fun getCustomCodeDetail(customCodeId: String): CustomCodeDto {
        val customCodeEntity = customCodeRepository.findById(customCodeId).orElse(CustomCodeEntity())
        val usedCustomCodeIdList = getUsedCustomCodeIdList()
        val customCodeDto = customCodeMapper.toCustomCodeDto(customCodeEntity)
        customCodeDto.enabled = (usedCustomCodeIdList.indexOf(customCodeEntity.customCodeId) == -1)
        when (customCodeDto.type) {
            CustomCodeConstants.Type.TABLE.code -> {
                customCodeDto.targetTableName =
                    customCodeTableRepository.findByCustomCodeTable(customCodeDto.targetTable!!).customCodeTableName
                customCodeDto.searchColumnName = getCustomCodeColumnName(
                    customCodeDto.targetTable!!,
                    customCodeDto.searchColumn!!,
                    CustomCodeConstants.ColumnType.SEARCH.code
                )
                customCodeDto.valueColumnName = getCustomCodeColumnName(
                    customCodeDto.targetTable!!,
                    customCodeDto.valueColumn!!,
                    CustomCodeConstants.ColumnType.VALUE.code
                )
            }
        }
        return customCodeDto
    }

    /**
     * 사용자 정의 코드 저장(등록/수정).
     *
     * @param customCodeDto
     * @return String
     */
    @Transactional
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
    @Transactional
    fun deleteCustomCode(customCodeId: String): String {
        return if (getUsedCustomCodeIdList().indexOf(customCodeId) != -1) {
            CustomCodeConstants.Status.STATUS_ERROR_CUSTOM_CODE_USED.code
        } else {
            customCodeRepository.deleteById(customCodeId)
            CustomCodeConstants.Status.STATUS_SUCCESS.code
        }
    }

    /**
     * 사용자 정의 코드 테이블 리스트 조회.
     *
     * @return MutableList<CustomCodeTableDto>
     */
    fun getCustomCodeTableList(): MutableList<CustomCodeTableDto> {
        val customCodeTableEntityList = customCodeTableRepository.findByOrderByCustomCodeTableNameAsc()
        val customCodeTableList = mutableListOf<CustomCodeTableDto>()
        for (customCodeTableEntity in customCodeTableEntityList) {
            customCodeTableList.add(customCodeTableMapper.toCustomCodeTableDto(customCodeTableEntity))
        }
        return customCodeTableList
    }

    /**
     * 사용자 정의 코드 컬럼 리스트 조회.
     *
     * @return MutableList<CustomCodeColumnDto>
     */
    fun getCustomCodeColumnList(targetTable: String? = null): MutableList<CustomCodeColumnDto> {
        val customCodeColumnEntityList = if (targetTable == null) {
            customCodeColumnRepository.findByOrderByCustomCodeColumnNameAsc()
        } else {
            customCodeColumnRepository.findByCustomCodeTable(targetTable)
        }
        val customCodeColumnList = mutableListOf<CustomCodeColumnDto>()
        for (customCodeColumnEntity in customCodeColumnEntityList) {
            customCodeColumnList.add(customCodeColumnMapper.toCustomCodeColumnDto(customCodeColumnEntity))
        }
        return customCodeColumnList
    }

    /**
     * 사용자 정의 조건에 부합하는 데이터 조회.
     *
     * @param customCodeId 커스텀 코드 ID
     * @return List<CustomCodeDataDto>
     */
    fun getCustomCodeData(customCodeId: String): List<CustomCodeDataDto> {
        val customCode = customCodeRepository.findByCustomCode(customCodeId)
        return if (customCode.type == CustomCodeConstants.Type.TABLE.code) {
            getTableTypeData(customCode)
        } else {
            getCodeTypeData(customCode)
        }
    }

    /**
     * 사용중인 사용자 정의 코드 ID 리스트 조회.
     *
     * @return List<String>
     */
    private fun getUsedCustomCodeIdList(): List<String> {
        val parameters = LinkedHashMap<String, Any>()
        parameters["componentType"] = CustomCodeConstants.COMPONENT_TYPE_CUSTOM_CODE
        parameters["componentAttribute"] = CustomCodeConstants.ATTRIBUTE_ID_DISPLAY
        return componentService.getComponentDataCustomCodeIds(parameters)
    }

    /**
     * 사용자 정의 코드 컬럼 이름 조회.
     *
     * @return String
     */
    private fun getCustomCodeColumnName(
        customCodeTable: String,
        customCodeColumn: String,
        customCodeType: String
    ): String {
        return customCodeColumnRepository.findById(
            CustomCodeColumnPk(
                customCodeTable = customCodeTable,
                customCodeColumn = customCodeColumn,
                customCodeType = customCodeType
            )
        ).orElse(CustomCodeColumnEntity()).customCodeColumnName
    }

    /**
     * 저장 가능한 데이터인지 확인.
     *
     * @param customCodeDto
     * @return String
     */
    private fun customCodeEditValid(customCodeDto: CustomCodeDto): String {
        var code = CustomCodeConstants.Status.STATUS_VALID_SUCCESS.code
        var isContinue = true
        val customCodeId = customCodeDto.customCodeId
        if (customCodeId != "" && getUsedCustomCodeIdList().indexOf(customCodeId) != -1) {
            code = CustomCodeConstants.Status.STATUS_ERROR_CUSTOM_CODE_USED.code
            isContinue = false
        }
        if (isContinue) {
            var isExistName = false
            if (customCodeId != "") {
                val existCustomCode = customCodeRepository.findById(customCodeId).orElse(CustomCodeEntity())
                isExistName = (customCodeDto.customCodeName == existCustomCode.customCodeName)
            }
            if (!isExistName && customCodeRepository.existsByCustomCodeName(customCodeDto.customCodeName!!)) {
                code = CustomCodeConstants.Status.STATUS_ERROR_CUSTOM_CODE_NAME_DUPLICATION.code
                isContinue = false
            }
        }
        if (isContinue && customCodeDto.type == CustomCodeConstants.Type.CODE.code &&
            !codeRepository.existsByPCode(CodeEntity(code = customCodeDto.pCode!!))
        ) {
            code = CustomCodeConstants.Status.STATUS_ERROR_CUSTOM_CODE_P_CODE_NOT_EXIST.code
        }
        return code
    }

    /**
     * 타입이 테이블인 데이터 조회.
     *
     * @param customCode CustomCodeEntity
     * @return MutableList<CustomCodeDataDto>
     */
    private fun getTableTypeData(customCode: CustomCodeCoreDto): MutableList<CustomCodeDataDto> {
        val customDataList = mutableListOf<CustomCodeDataDto>()
        var dataList = mutableListOf<Any>()
        val condition = jsonToArrayByCondition(customCode.condition)
        val sort = Sort(Sort.Direction.ASC, toCamelCase(customCode.searchColumn!!))
        when (customCode.targetTable) {
            CustomCodeConstants.TableName.ROLE.code -> {
                dataList = roleRepository.findAll(RoleCustomCodeSpecification(condition), sort).toMutableList()
            }
            CustomCodeConstants.TableName.USER.code -> {
                dataList = userRepository.findAll(UserCustomCodeSpecification(condition), sort).toMutableList()
            }
        }
        if (dataList.size > 0) {
            for (data in dataList) {
                val customCodeDataDto = CustomCodeDataDto(key = "", value = "")
                val dataFields = data::class.java.declaredFields
                for (dataField in dataFields) {
                    if (dataField.isAnnotationPresent(Column::class.java)) {
                        dataField.isAccessible = true
                        val columnName = dataField.getAnnotation(Column::class.java)?.name
                        if (columnName == customCode.valueColumn) { // key
                            customCodeDataDto.key = dataField.get(data) as? String ?: ""
                        }
                        if (columnName == customCode.searchColumn) { // value
                            customCodeDataDto.value = dataField.get(data) as? String ?: ""
                        }
                        dataField.isAccessible = false
                    }
                }
                if (customCodeDataDto.key.isNotEmpty() && customCodeDataDto.value.isNotEmpty()) {
                    customDataList.add(customCodeDataDto)
                }
            }
        }
        return customDataList
    }

    /**
     * 타입이 코드인 데이터 조회.
     *
     * @param customCode CustomCodeEntity
     * @return MutableList<CustomCodeDataDto>
     */
    private fun getCodeTypeData(customCode: CustomCodeCoreDto): MutableList<CustomCodeDataDto> {
        val customDataList = mutableListOf<CustomCodeDataDto>()
        val dataList = customCode.pCode?.let { codeService.getCodeListByCustomCode(it) }
        dataList?.forEach {
            customDataList.add(CustomCodeDataDto(key = it.code, value = it.codeValue!!))
        }
        return customDataList
    }

    /**
     * Json to Map.
     *
     * @param condition 조건
     * @return MutableMap<String, Any>
     */
    private fun jsonToArrayByCondition(condition: String?): Array<CustomCodeConditionDto>? {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val jsonToArray = mapper.readValue(condition, Array<CustomCodeConditionDto>::class.java)
        jsonToArray?.forEach {
            it.conditionKey = toCamelCase(it.conditionKey)
            if (it.conditionKey == "useYn") {
                it.conditionValue = it.conditionValue == "true"
            }
        }
        return jsonToArray
    }

    /**
     * To Camel Case.
     *
     * @param text
     * @return String
     */
    private fun toCamelCase(text: String): String {
        var camelText = text
        while (camelText.indexOf("_") > -1) {
            camelText = camelText.replaceFirst(
                "_([a-zA-Z])".toRegex(),
                camelText[camelText.indexOf("_") + 1].toUpperCase().toString()
            )
        }
        return camelText
    }
}
