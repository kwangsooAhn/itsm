/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ci.service

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.cmdb.ci.repository.CIDataRepository
import co.brainz.cmdb.ciAttribute.constants.CIAttributeConstants
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.ciType.constants.CITypeConstants
import co.brainz.cmdb.ciType.repository.CITypeRepository
import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.dto.CIContentDto
import co.brainz.cmdb.dto.CIDynamicListDto
import co.brainz.cmdb.dto.CISearchItem
import co.brainz.cmdb.dto.CIsDto
import co.brainz.framework.util.AliceMessageSource
import co.brainz.itsm.cmdb.ci.dto.CISearch
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
import org.springframework.stereotype.Service

@Service
class CISearchService(
    private val aliceMessageSource: AliceMessageSource,
    private val ciTypeService: CITypeService,
    private val ciTypeRepository: CITypeRepository,
    private val ciDataRepository: CIDataRepository,
    private val ciAttributeRepository: CIAttributeRepository
) {

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
                    //CIAttributeConstants.Column.CI_TYPE_NAME.value -> content.add(ci.typeName)
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
        //columnNameList.add(CIAttributeConstants.Column.CI_TYPE_NAME.value)
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
        //columnTitleList.add(aliceMessageSource.getMessage("cmdb.ci.label.ciType"))
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
        columnTypeList.add(CIAttributeConstants.Type.STRING.code)
        //columnTypeList.add(CIAttributeConstants.Type.STRING.code)
        columnTypeList.add(CIAttributeConstants.Type.STRING.code)
        columnTypeList.add(CIAttributeConstants.Type.STRING.code)
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
        //columnWidthList.add(CIAttributeConstants.Width.CI_TYPE_NAME.width)
        columnWidthList.add(CIAttributeConstants.Width.CI_NAME.width)
        columnWidthList.add(CIAttributeConstants.Width.CI_DESC.width)
        return columnWidthList
    }

    /**
     * 동적 데이터 생성
     */
    fun getDynamic(typeId: String, basic: CIDynamicListDto, searchItemsData: CISearch): CIDynamicListDto {
        val dynamic = this.initDynamic(typeId)
        if (dynamic.columnName.isNotEmpty()) {
            dynamic.searchItems.addAll(this.getSearchItems(dynamic.columnName, searchItemsData))
            dynamic.contents.addAll(this.getDynamicContents(basic, dynamic.columnName))
        }
        return dynamic
    }

    private fun initDynamic(typeId: String): CIDynamicListDto {
        val dynamic = CIDynamicListDto()
        // root 는 동적 데이터를 적용하지 않는다.
        if (typeId != CITypeConstants.CI_TYPE_ROOT_ID) {
            val ciTypeEntity = ciTypeRepository.findById(typeId).orElse(null)
            val attributeList = ciTypeEntity.ciClass.ciClassAttributeMapEntities.sortedBy { it.attributeOrder }
            attributeList.forEach {
                if (it.ciAttribute.searchYn) {
                    dynamic.columnName.add(it.ciAttribute.attributeId)
                    dynamic.columnTitle.add(it.ciAttribute.attributeText ?: "") //attributeName 대신 attributeText
                    dynamic.columnWidth.add(it.ciAttribute.searchWidth + "px")
                    dynamic.columnType.add(
                        when (it.ciAttribute.attributeType) { // date, datetime 제외한 컬럼은 모두 string
                            CIAttributeConstants.Type.DATE.code -> CIAttributeConstants.Type.DATE.code
                            CIAttributeConstants.Type.DATE_TIME.code -> CIAttributeConstants.Type.DATE_TIME.code
                            else -> CIAttributeConstants.Type.STRING.code
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
    fun getDynamicContents(basic: CIDynamicListDto, columnName: ArrayList<String>): MutableList<CIContentDto> {
        // 전체 CI DATA 조회
        val ciIds = mutableSetOf<String>()
        basic.contents.forEach { ciIds.add(it.key) }
        val allCIDataList = ciDataRepository.findCIDataList(ciIds)

        val contents = mutableListOf<CIContentDto>()
        basic.contents.forEach { content ->
            // 현재 CI_ID 의 CI_DATA 조회
            val ciDataList = mutableListOf<CIDataEntity>()
            allCIDataList.forEach { ciData ->
                if (ciData.ciAttribute.searchYn && content.key == ciData.ci.ciId) {
                    ciDataList.add(ciData)
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
                if (!isValid) { // 값이 존재하지 않을 경우 null로 해당 순번 채우기
                    value.add(null)
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
}
