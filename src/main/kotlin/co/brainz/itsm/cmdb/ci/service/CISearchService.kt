/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ci.service

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.cmdb.ci.repository.CIDataRepository
import co.brainz.cmdb.ci.repository.CIGroupListDataRepository
import co.brainz.cmdb.ciAttribute.constants.CIAttributeConstants
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.ciType.constants.CITypeConstants
import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.dto.CIContentDto
import co.brainz.cmdb.dto.CIDynamicListDto
import co.brainz.cmdb.dto.CIGroupListDataDto
import co.brainz.cmdb.dto.CISearchItem
import co.brainz.cmdb.dto.CIsDto
import co.brainz.framework.querydsl.QuerydslConstants
import co.brainz.framework.util.AliceMessageSource
import co.brainz.itsm.cmdb.ci.dto.CISearch
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service

@Service
class CISearchService(
    private val aliceMessageSource: AliceMessageSource,
    private val ciTypeService: CITypeService,
    private val ciTypeRepository: CITypeRepository,
    private val ciDataRepository: CIDataRepository,
    private val ciAttributeRepository: CIAttributeRepository,
    private val ciGroupListDataRepository: CIGroupListDataRepository
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 공통 출력 컬럼 조회
     */
    fun getBasic(ciList: List<CIsDto>): CIDynamicListDto {
        val columnName = this.basicColumnName()
        return CIDynamicListDto(
            columnName = columnName,
            columnTitle = this.basicColumnTitle(),
            columnType = this.basicColumnType(),
            columnWidth = this.basicColumnWidth(),
            contents = this.getBasicContent(columnName, ciList)
        )
    }

    /**
     * 공통 컬럼 데이터 생성
     */
    private fun getBasicContent(columnName: ArrayList<String>, ciList: List<CIsDto>): MutableList<CIContentDto> {
        val contents = mutableListOf<CIContentDto>()
        ciList.forEach { ci ->
            val content = arrayListOf<Any?>()
            columnName.forEach { column ->
                when (column) {
                    CIAttributeConstants.Column.CI_ICON.value ->
                        content.add(ci.ciIcon?.let { ciTypeService.getCITypeImageData(it) })
                    CIAttributeConstants.Column.CI_ID.value -> content.add(ci.ciId)
                    CIAttributeConstants.Column.CI_NO.value -> content.add(ci.ciNo)
                    CIAttributeConstants.Column.CI_NAME.value -> content.add(ci.ciName)
                    CIAttributeConstants.Column.CI_DESC.value -> content.add(ci.ciDesc)
                }
            }
            contents.add(
                CIContentDto(
                    key = ci.ciId,
                    value = content
                )
            )
        }
        return contents
    }

    /**
     * 공통 컬럼 생성
     */
    private fun basicColumnName(): ArrayList<String> {
        val columnNameList = arrayListOf<String>()
        columnNameList.add(CIAttributeConstants.Column.CI_ICON.value)
        columnNameList.add(CIAttributeConstants.Column.CI_ID.value)
        columnNameList.add(CIAttributeConstants.Column.CI_NO.value)
        columnNameList.add(CIAttributeConstants.Column.CI_NAME.value)
        columnNameList.add(CIAttributeConstants.Column.CI_DESC.value)
        return columnNameList
    }

    /**
     * 공통 타이틀 생성
     */
    private fun basicColumnTitle(): ArrayList<String> {
        val columnTitleList = arrayListOf<String>()
        columnTitleList.add("")
        columnTitleList.add("")
        columnTitleList.add(aliceMessageSource.getMessage("cmdb.ci.label.ciNo"))
        columnTitleList.add(aliceMessageSource.getMessage("cmdb.ci.label.ciName"))
        columnTitleList.add(aliceMessageSource.getMessage("cmdb.ci.label.ciDesc"))
        return columnTitleList
    }

    /**
     * 공통 타입 생성
     */
    private fun basicColumnType(): ArrayList<String> {
        val columnTypeList = arrayListOf<String>()
        columnTypeList.add(CIAttributeConstants.Type.ICON.code)
        columnTypeList.add(CIAttributeConstants.Type.HIDDEN.code)
        columnTypeList.add(CIAttributeConstants.Type.INPUT_BOX.code)
        //columnTypeList.add(CIAttributeConstants.Type.INPUTBOX.code)
        columnTypeList.add(CIAttributeConstants.Type.INPUT_BOX.code)
        columnTypeList.add(CIAttributeConstants.Type.INPUT_BOX.code)
        return columnTypeList
    }

    /**
     * 공통 넓이 생성
     */
    private fun basicColumnWidth(): ArrayList<String> {
        val columnWidthList = arrayListOf<String>()
        columnWidthList.add(CIAttributeConstants.Width.CI_ICON.width)
        columnWidthList.add(CIAttributeConstants.Width.CI_ID.width)
        columnWidthList.add(CIAttributeConstants.Width.CI_NO.width)
        columnWidthList.add(CIAttributeConstants.Width.CI_NAME.width)
        columnWidthList.add(CIAttributeConstants.Width.CI_DESC.width)
        return columnWidthList
    }

    /**
     * 동적 데이터 생성
     */
    fun getDynamic(
        typeId: String,
        basic: CIDynamicListDto,
        searchItemsData: CISearch,
        isExcel: Boolean
    ): CIDynamicListDto {
        val dynamic = this.initDynamic(typeId, isExcel)
        if (dynamic.columnName.isNotEmpty()) {
            dynamic.searchItems.addAll(this.getSearchItems(dynamic.columnName, searchItemsData))
            dynamic.contents.addAll(this.getDynamicContents(basic, dynamic.columnName, isExcel))
        }
        return dynamic
    }

    private fun initDynamic(typeId: String, isExcel: Boolean): CIDynamicListDto {
        val dynamic = CIDynamicListDto()
        // root 는 동적 데이터를 적용하지 않는다.
        if (typeId != CITypeConstants.CI_TYPE_ROOT_ID) {
            val ciTypeEntity = ciTypeRepository.findById(typeId).orElse(null)
            val attributeList = ciTypeEntity.ciClass.ciClassAttributeMapEntities.sortedBy { it.attributeOrder }
            attributeList.forEach {
                if (isExcel || it.ciAttribute.searchYn) {
                    dynamic.columnName.add(it.ciAttribute.attributeId)
                    dynamic.columnTitle.add(it.ciAttribute.attributeText ?: "") //attributeName 대신 attributeText
                    dynamic.columnWidth.add(it.ciAttribute.searchWidth + CIAttributeConstants.UNIT_PX)
                    dynamic.columnType.add(
                        when (it.ciAttribute.attributeType) {
                            CIAttributeConstants.Type.CHECKBOX.code -> CIAttributeConstants.Type.CHECKBOX.code
                            CIAttributeConstants.Type.CUSTOM_CODE.code -> CIAttributeConstants.Type.CUSTOM_CODE.code
                            CIAttributeConstants.Type.DATE.code -> CIAttributeConstants.Type.DATE.code
                            CIAttributeConstants.Type.DATE_TIME.code -> CIAttributeConstants.Type.DATE_TIME.code
                            CIAttributeConstants.Type.DROP_DOWN.code -> CIAttributeConstants.Type.DROP_DOWN.code
                            CIAttributeConstants.Type.GROUP_LIST.code -> CIAttributeConstants.Type.GROUP_LIST.code
                            CIAttributeConstants.Type.RADIO.code -> CIAttributeConstants.Type.RADIO.code
                            CIAttributeConstants.Type.USER_SEARCH.code -> CIAttributeConstants.Type.USER_SEARCH.code
                            CIAttributeConstants.Type.ORGANIZATION_SEARCH.code -> CIAttributeConstants.Type.ORGANIZATION_SEARCH.code
                            else -> CIAttributeConstants.Type.INPUT_BOX.code
                        }
                    )
                }
            }
        }
        return dynamic
    }

    /**
     * 동적 데이터 생성 (전체 CI DATA 조회 후 CI 별 DATA 비교 후 동적 데이터 생성)
     */
    fun getDynamicContents(basic: CIDynamicListDto, columnName: ArrayList<String>, isExcel: Boolean): MutableList<CIContentDto> {
        // 전체 CI DATA 조회
        val ciIds = mutableSetOf<String>()
        basic.contents.forEach { ciIds.add(it.key) }
        val allCIDataList = ciDataRepository.findCIDataList(ciIds)

        val contents = mutableListOf<CIContentDto>()
        basic.contents.forEach { content ->
            // 현재 CI_ID 의 CI_DATA 조회
            val ciDataList = mutableListOf<CIDataEntity>()
            allCIDataList.forEach { ciData ->
                if (content.key == ciData.ci.ciId) {
                    if (isExcel) {
                        ciDataList.add(ciData)
                    } else {
                        if (ciData.ciAttribute.searchYn) {
                            ciDataList.add(ciData)
                        }
                    }
                }
            }

            // 값 설정
            val value = arrayListOf<Any?>()
            columnName.forEach {
                var isValid = false
                ciDataList.forEach { ciData ->
                    if (it == ciData.ciAttribute.attributeId) {
                        isValid = true
                        value.add(this.getValue(ciData))
                    }
                }
                if (!isValid) { // 값이 존재하지 않을 경우 빈값으로 해당 순번 채우기
                    value.add("")
                }
            }

            contents.add(
                CIContentDto(
                    key = content.key,
                    value = value
                )
            )
        }
        return contents
    }

    /**
     * 항목 타입에 따라 값 처리
     *   - 타입에 따라 변환이 필요한 경우 사용
     */
    private fun getValue(ciData: CIDataEntity): Any? {
        return when (ciData.ciAttribute.attributeType) {
            CIAttributeConstants.Type.DATE.code -> ciData.value
            CIAttributeConstants.Type.DATE_TIME.code -> ciData.value
            else -> ciData.value
        }
    }

    /**
     * 상세 검색 속성 정보 조회
     */
    fun getSearchItems(columnName: ArrayList<String>, searchItemsData: CISearch): List<CISearchItem> {
        val attributeIds = mutableSetOf<String>()
        columnName.forEach { attributeIds.add(it) }
        val attributeList = ciAttributeRepository.findAttributeList(attributeIds)
        attributeList.forEach { attribute ->
            searchItemsData.searchItems?.forEach { searchItem ->
                if (searchItem.attributeId == attribute.attributeId) {
                    attribute.searchValue = searchItem.searchValue
                }
            }
        }
        // 정렬
        val sortAttributeList = mutableListOf<CISearchItem>()
        columnName.forEach { name ->
            attributeList.forEach { attribute ->
                if (name == attribute.attributeId) {
                    sortAttributeList.add(attribute)
                }
            }
        }

        return sortAttributeList
    }

    /**
     * 동적 데이터 조회 필터링
     */
    fun getFilterContents(basic: CIDynamicListDto): MutableList<CIContentDto> {
        val contents = basic.contents
        val removeIndexes = mutableSetOf<Int>()
        basic.searchItems.forEach { searchItem ->
            if (searchItem.searchValue.isNotEmpty()) {
                var idx = -1
                run loop@{
                    basic.columnName.forEachIndexed { index, column ->
                        if (column == searchItem.attributeId) {
                            idx = index
                        }
                    }
                }

                if (idx > -1) {
                    basic.contents.forEachIndexed { index, content ->
                        when (searchItem.attributeType) {
                            CIAttributeConstants.Type.RADIO.code,
                            CIAttributeConstants.Type.DROP_DOWN.code -> {
                                if (searchItem.searchValue != content.value[idx]?.toString()) {
                                    removeIndexes.add(index)
                                }
                            }
                            else -> {
                                if (content.value[idx]?.toString()?.contains(searchItem.searchValue) == false) {
                                    removeIndexes.add(index)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (removeIndexes.isNotEmpty()) {
            removeIndexes.sortedDescending().forEach {
                contents.removeAt(it)
            }
        }

        return contents
    }

    /**
     * 공통 데이터에 공적 데이터 반영
     */
    fun addDynamic(basic: CIDynamicListDto, dynamic: CIDynamicListDto?): CIDynamicListDto {
        if (dynamic?.columnName?.isNotEmpty() == true) {
            basic.searchItems.addAll(dynamic.searchItems)
            basic.columnName.addAll(dynamic.columnName)
            basic.columnTitle.addAll(dynamic.columnTitle)
            basic.columnType.addAll(dynamic.columnType)
            basic.columnWidth.addAll(dynamic.columnWidth)
            basic.contents.forEach { content ->
                dynamic.contents.forEach { attributeContent ->
                    if (content.key == attributeContent.key) {
                        content.value.addAll(attributeContent.value)
                    }
                }
            }
        }
        return basic
    }

    /**
     * 페이징 처리
     */
    fun getPaging(basic: CIDynamicListDto, ciSearchCondition: CISearchCondition): MutableList<CIContentDto> {
        val offset = (ciSearchCondition.pageNum - 1) * ciSearchCondition.contentNumPerPage
        val limit = ciSearchCondition.contentNumPerPage
        val startIndex = offset.toInt()
        var endIndex = (startIndex + limit).toInt()
        if (basic.contents.size < endIndex) {
            endIndex = basic.contents.size
        }
        return basic.contents.subList(startIndex, endIndex)
    }

    /**
     * 코드 값을 출력할 값으로 변경
     */
    fun getConvertValue(basic: CIDynamicListDto): MutableList<CIContentDto> {
        val attributeIds = mutableSetOf<String>()
        basic.columnType.forEachIndexed { index, type ->
            when (type) {
                CIAttributeConstants.Type.RADIO.code,
                CIAttributeConstants.Type.CHECKBOX.code,
                CIAttributeConstants.Type.CUSTOM_CODE.code,
                CIAttributeConstants.Type.GROUP_LIST.code,
                CIAttributeConstants.Type.DROP_DOWN.code,
                CIAttributeConstants.Type.USER_SEARCH.code,
                CIAttributeConstants.Type.ORGANIZATION_SEARCH.code -> {
                    attributeIds.add(basic.columnName[index])
                }
            }
        }
        val attributeList = ciAttributeRepository.findAttributeList(attributeIds)

        basic.columnType.forEachIndexed { index, type ->
            when (type) {
                CIAttributeConstants.Type.CHECKBOX.code,
                CIAttributeConstants.Type.DROP_DOWN.code,
                CIAttributeConstants.Type.RADIO.code -> {
                    val attribute = this.getAttribute(attributeList, basic.columnName[index])
                    if (attribute.attributeId.isNotEmpty()) {
                        val valueMap: Map<String, Any> =
                            mapper.readValue(attribute.attributeValue, object : TypeReference<Map<String, Any>>() {})
                        val options: List<Map<String, Any>> =
                            mapper.convertValue(valueMap["option"], object : TypeReference<List<Map<String, Any>>>() {})
                        basic.contents.forEach {
                            val contentValue = it.value[index].toString().trim()
                            if (contentValue.isNotEmpty()) {
                                val values = contentValue.split(",")
                                val convertValueList = arrayListOf<String>()
                                values.forEach {
                                    options.forEach { option ->
                                        if (it == option["value"].toString()) {
                                            convertValueList.add(option["text"].toString())
                                        }
                                    }
                                }
                                it.value[index] = convertValueList.joinToString(separator = ", ")
                            }
                        }
                    }
                }
                CIAttributeConstants.Type.GROUP_LIST.code -> {
                    val attribute = this.getAttribute(attributeList, basic.columnName[index])
                    if (attribute.attributeId.isNotEmpty()) {
                        val valueMap: Map<String, Any> =
                            mapper.readValue(attribute.attributeValue, object : TypeReference<Map<String, Any>>() {})
                        val options: List<Map<String, Any>> =
                            mapper.convertValue(valueMap["option"], object : TypeReference<List<Map<String, Any>>>() {})
                        val cAttributeIds = mutableSetOf<String>()
                        val ciIds = mutableSetOf<String>()
                        options.forEach { cAttributeIds.add(it["id"].toString()) }
                        basic.contents.forEach { ciIds.add(it.key) }
                        // 전체 그룹 리스트 데이터 조회
                        val ciGroupListDataList =
                            ciGroupListDataRepository.getCIGroupListDataList(ciIds, attribute.attributeId, cAttributeIds)
                        basic.contents.forEach { content ->
                            // ci_id 에 일치하는 목록만 추출
                            val groupListDataList = mutableListOf<CIGroupListDataDto>()
                            ciGroupListDataList.forEach { groupData ->
                                if (groupData.ciId == content.key) {
                                    groupListDataList.add(groupData)
                                }
                            }
                            groupListDataList.sortWith(compareBy<CIGroupListDataDto> { it.cAttributeSeq }.thenBy { it.cAttributeId })
                            val valueArray = arrayListOf<String>()
                            var value = ""
                            var seq = 0
                            groupListDataList.forEachIndexed { index, it ->
                                if (seq == it.cAttributeSeq) {
                                    if (value.isNotEmpty()) {
                                        value += ", "
                                    }
                                } else {
                                    valueArray.add(value)
                                    value = ""
                                }
                                value += it.cAttributeText + ": " + it.cValue
                                if (index == groupListDataList.size - 1) {
                                    valueArray.add(value)
                                } else {
                                    seq = it.cAttributeSeq
                                }
                            }
                            var strValue = ""
                            valueArray.forEachIndexed { index, it ->
                                strValue += "[$it]"
                                if (index != valueArray.size - 1) {
                                    strValue += ", "
                                }
                            }
                            content.value[index] = strValue
                        }
                    }
                }
                CIAttributeConstants.Type.CUSTOM_CODE.code -> {
                    val attribute = this.getAttribute(attributeList, basic.columnName[index])
                    if (attribute.attributeId.isNotEmpty()) {
                        basic.contents.forEach {
                            val attributeValue = it.value[index].toString()
                            if (attributeValue.isNotEmpty()) {
                                it.value[index] = attributeValue.substring(attributeValue.lastIndexOf("|") + 1)
                            }
                        }
                    }
                }
                CIAttributeConstants.Type.USER_SEARCH.code,
                CIAttributeConstants.Type.ORGANIZATION_SEARCH.code -> {
                    val attribute = this.getAttribute(attributeList, basic.columnName[index])
                    if (attribute.attributeId.isNotEmpty()) {
                        basic.contents.forEach {
                            val attributeValue = it.value[index].toString()
                            if (attributeValue.isNotEmpty()) {
                                it.value[index] = attributeValue.trim().split("|")[1]
                            }
                        }
                    }
                }
            }
        }

        return basic.contents
    }

    /**
     * 전체 속성정보에서 일치하는 속성 찾기
     */
    private fun getAttribute(attributeList: List<CISearchItem>, columnName: String): CISearchItem {
        var attribute = CISearchItem()
        run loop@{
            attributeList.forEach {
                if (columnName == it.attributeId) {
                    attribute = it
                    return@loop
                }
            }
        }
        return attribute
    }

    /**
     * 데이터 정렬
     */
    fun getOrderContents(basic: CIDynamicListDto, ciSearchCondition: CISearchCondition): MutableList<CIContentDto> {
        if (ciSearchCondition.orderColName?.isNotEmpty() == true) {
            var idx = -1
            basic.columnName.forEachIndexed { index, column ->
                if (column == ciSearchCondition.orderColName) {
                    idx = index
                }
            }
            if (idx > -1) {
                when (ciSearchCondition.orderDir) {
                    QuerydslConstants.OrderSpecifier.DESC.code -> {
                        basic.contents.sortByDescending { it.value[idx].toString() }
                    }
                    else -> {
                        basic.contents.sortBy { it.value[idx].toString() }
                    }
                }
            }
        }
        return basic.contents
    }
}
