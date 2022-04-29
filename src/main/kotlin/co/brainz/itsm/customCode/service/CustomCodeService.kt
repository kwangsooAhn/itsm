/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.service

import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.organization.specification.OrganizationCustomCodeSpecification
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.code.entity.CodeEntity
import co.brainz.itsm.code.repository.CodeRepository
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.customCode.constants.CustomCodeConstants
import co.brainz.itsm.customCode.dto.CustomCodeColumnDto
import co.brainz.itsm.customCode.dto.CustomCodeConditionDto
import co.brainz.itsm.customCode.dto.CustomCodeCoreDto
import co.brainz.itsm.customCode.dto.CustomCodeDto
import co.brainz.itsm.customCode.dto.CustomCodeListReturnDto
import co.brainz.itsm.customCode.dto.CustomCodeSearchCondition
import co.brainz.itsm.customCode.dto.CustomCodeTableDto
import co.brainz.itsm.customCode.dto.CustomCodeTreeDto
import co.brainz.itsm.customCode.dto.CustomCodeTreeReturnDto
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
import co.brainz.workflow.component.repository.WfComponentPropertyRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
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
    private val organizationRepository: OrganizationRepository,
    private val userRepository: UserRepository,
    private val codeRepository: CodeRepository,
    private val codeService: CodeService,
    private val currentSessionUser: CurrentSessionUser,
    private val wfComponentPropertyRepository: WfComponentPropertyRepository
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
                totalPageNum = ceil(queryResult.total.toDouble() / customCodeSearchCondition.contentNumPerPage.toDouble()).toLong(),
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
        val customCodeDto = customCodeMapper.toCustomCodeDto(customCodeEntity)
        customCodeDto.enabled = (this.getUsedCustomCodeIdList().contains(customCodeEntity.customCodeId))
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
     */
    @Transactional
    fun saveCustomCode(customCodeDto: CustomCodeDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (customCodeEditValid(customCodeDto)) {
            CustomCodeConstants.Status.STATUS_VALID_SUCCESS.code -> {
                customCodeRepository.save(customCodeMapper.toCustomCodeEntity(customCodeDto))
            }
            else -> {
                status = ZResponseConstants.STATUS.ERROR_FAIL
            }
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 사용자 정의 코드 삭제.
     *
     * @param customCodeId
     */
    @Transactional
    fun deleteCustomCode(customCodeId: String): ZResponse {
        val status = if (getUsedCustomCodeIdList().contains(customCodeId)) {
            ZResponseConstants.STATUS.ERROR_EXIST
        } else {
            customCodeRepository.deleteById(customCodeId)
            ZResponseConstants.STATUS.SUCCESS
        }
        return ZResponse(
            status = status.code
        )
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
    fun getCustomCodeData(customCodeId: String): CustomCodeTreeReturnDto {
        val customCode = customCodeRepository.findByCustomCode(customCodeId)
        return if (customCode.type == CustomCodeConstants.Type.TABLE.code) {
            this.getTableTypeData(customCode)
        } else {
            this.getCodeTypeData(customCode)
        }
    }

    /**
     * 사용중인 사용자 정의 코드 ID 리스트 조회.
     *
     * @return List<String>
     */
    fun getUsedCustomCodeIdList(): List<String> {
        val componentOptions = wfComponentPropertyRepository.findComponentTypeAndProperty(
            CustomCodeConstants.COMPONENT_TYPE_CUSTOM_CODE,
            CustomCodeConstants.PROPERTY_ID_ELEMENT
        )

        val customCodeIds = mutableSetOf<String>()
        componentOptions.forEach {
            val option: MutableMap<String, Any> = ObjectMapper().readValue(it)
            customCodeIds.add(option["defaultValueCustomCode"].toString().split("|")[0])
        }

        return customCodeIds.toList()
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
        if (customCodeId != "" && getUsedCustomCodeIdList().contains(customCodeId)) {
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
    fun getTableTypeData(customCode: CustomCodeCoreDto): CustomCodeTreeReturnDto {
        var customDataList = mutableListOf<CustomCodeTreeDto>()
        var dataList = mutableListOf<Any>()
        val condition = jsonToArrayByCondition(customCode.condition)
        val sort = Sort(Sort.Direction.ASC, toCamelCase(customCode.searchColumn!!))
        when (customCode.targetTable) {
            CustomCodeConstants.TableName.ROLE.code -> {
                val roleList = roleRepository.findAll(RoleCustomCodeSpecification(condition), sort).toMutableList()
                customDataList = this.setCustomCodeTreeByRole(customCode, roleList)
            }
            CustomCodeConstants.TableName.USER.code -> {
                val userList = userRepository.findAll(UserCustomCodeSpecification(condition), sort).toMutableList()
                customDataList = this.setCustomCodeTreeByUser(customCode, userList)
            }
            CustomCodeConstants.TableName.ORGANIZATION.code -> {
                val organizationList =
                    organizationRepository.findAll(OrganizationCustomCodeSpecification(condition), sort).toMutableList()
                customDataList = this.setCustomCodeTreeByOrganization(customCode, organizationList)
            }
        }
        return CustomCodeTreeReturnDto(
            data = customDataList,
            totalCount = customDataList.size.toLong()
        )
    }

    private fun setCustomCodeTreeByOrganization(
        customCode: CustomCodeCoreDto,
        dataList: List<OrganizationEntity>
    ): MutableList<CustomCodeTreeDto> {
        val customDataList = mutableListOf<CustomCodeTreeDto>()
        if (dataList.isNotEmpty()) {
            customDataList.add(
                CustomCodeTreeDto(
                    code = customCode.customCodeId,
                    codeName = customCode.customCodeName,
                    level = 0,
                    seqNum = 0
                )
            )
            dataList.forEachIndexed { index, data ->
                customDataList.add(
                    CustomCodeTreeDto(
                        code = data.organizationId,
                        pCode = customCode.customCodeId,
                        codeName = data.organizationName,
                        level = 1,
                        seqNum = index
                    )
                )
            }
        }
        return customDataList
    }

    private fun setCustomCodeTreeByRole(
        customCode: CustomCodeCoreDto,
        dataList: List<AliceRoleEntity>
    ): MutableList<CustomCodeTreeDto> {
        val customDataList = mutableListOf<CustomCodeTreeDto>()
        if (dataList.isNotEmpty()) {
            customDataList.add(
                CustomCodeTreeDto(
                    code = customCode.customCodeId,
                    codeName = customCode.customCodeName,
                    level = 0,
                    seqNum = 0
                )
            )
            dataList.forEachIndexed { index, data ->
                customDataList.add(
                    CustomCodeTreeDto(
                        code = data.roleId,
                        pCode = customCode.customCodeId,
                        codeName = data.roleName,
                        level = 1,
                        seqNum = index
                    )
                )
            }
        }
        return customDataList
    }

    private fun setCustomCodeTreeByUser(
        customCode: CustomCodeCoreDto,
        dataList: List<AliceUserEntity>
    ): MutableList<CustomCodeTreeDto> {
        val customDataList = mutableListOf<CustomCodeTreeDto>()
        if (dataList.isNotEmpty()) {
            customDataList.add(
                CustomCodeTreeDto(
                    code = customCode.customCodeId,
                    codeName = customCode.customCodeName,
                    level = 0,
                    seqNum = 0
                )
            )
            dataList.forEachIndexed { index, data ->
                customDataList.add(
                    CustomCodeTreeDto(
                        code = data.userKey,
                        pCode = customCode.customCodeId,
                        codeName = data.userName,
                        level = 1,
                        seqNum = index
                    )
                )
            }
        }
        return customDataList
    }

    /**
     * 타입인 테이블의 데이터를 트리로 변환.
     *
     * @param customCode CustomCodeEntity
     * @param dataList MutableList<Any>
     * @return MutableList<CustomCodeTreeDto>
     */
    private fun convertTableTypeDataToTree(
        customCode: CustomCodeCoreDto,
        dataList: MutableList<Any>
    ): MutableList<CustomCodeTreeDto> {
        val customDataTree = mutableListOf<CustomCodeTreeDto>()
        // 부모 코드 추가
        customDataTree.add(
            CustomCodeTreeDto(
                code = customCode.customCodeId,
                codeName = customCode.customCodeName,
                level = 0,
                seqNum = 0
            )
        )
        for ((index, data) in dataList.withIndex()) {
            val customCodeDataDto = CustomCodeTreeDto(
                code = "",
                pCode = customCode.customCodeId,
                codeName = "",
                level = 1,
                seqNum = index
            )
            val dataFields = data::class.java.declaredFields
            for (dataField in dataFields) {
                if (dataField.isAnnotationPresent(Column::class.java)) {
                    dataField.isAccessible = true
                    val columnName = dataField.getAnnotation(Column::class.java)?.name
                    if (columnName == customCode.valueColumn) {
                        customCodeDataDto.code = dataField.get(data) as? String ?: ""
                    }
                    if (columnName == customCode.searchColumn) {
                        customCodeDataDto.codeName = dataField.get(data) as? String ?: ""
                    }
                    dataField.isAccessible = false
                }
            }
            if (customCodeDataDto.code.isNotEmpty() && !customCodeDataDto.codeName.isNullOrEmpty()) {
                customDataTree.add(customCodeDataDto)
            }
        }

        return customDataTree
    }

    /**
     * 타입이 코드인 데이터 조회.
     *
     * @param customCode CustomCodeEntity
     * @return MutableList<CustomCodeDataDto>
     */
    fun getCodeTypeData(customCode: CustomCodeCoreDto): CustomCodeTreeReturnDto {
        val customDataList = mutableListOf<CustomCodeTreeDto>()
        val lang = currentSessionUser.getUserDto()?.lang

        if (customCode.pCode != null) {
            val dataList = codeService.getCodeList("", customCode.pCode!!)
            val pCodeIds = dataList.data.map { it.code }
            // 다국어 코드 조회
            val codeLangDataList = codeRepository.findCodeByCodeLang(pCodeIds.toSet(), lang)
            // 다국어 코드 병합
            for (data in codeLangDataList) {
                customDataList.add(
                    CustomCodeTreeDto(
                        code = data.code,
                        pCode = data.pCode,
                        codeValue = data.codeValue,
                        codeName = data.codeLangName ?: data.codeName,
                        codeDesc = data.codeDesc,
                        editable = data.editable,
                        level = data.level,
                        seqNum = data.seqNum
                    )
                )
            }
        }

        return CustomCodeTreeReturnDto(
            data = customDataList,
            totalCount = customDataList.size.toLong()
        )
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
