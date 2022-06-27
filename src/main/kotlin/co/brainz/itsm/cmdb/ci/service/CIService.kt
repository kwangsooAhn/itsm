/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.service

import co.brainz.api.dto.RequestCIComponentVO
import co.brainz.cmdb.ci.repository.CIRepository
import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.ciAttribute.constants.CIAttributeConstants
import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIAttributeValueDto
import co.brainz.cmdb.dto.CIAttributeValueGroupListDto
import co.brainz.cmdb.dto.CIClassDetailValueDto
import co.brainz.cmdb.dto.CIContentDto
import co.brainz.cmdb.dto.CIDetailDto
import co.brainz.cmdb.dto.CIDynamicListDto
import co.brainz.cmdb.dto.CIDynamicReturnDto
import co.brainz.cmdb.dto.CIHistoryDto
import co.brainz.cmdb.dto.CIModalReturnDto
import co.brainz.cmdb.dto.CIRelationDto
import co.brainz.cmdb.dto.CISearchItem
import co.brainz.cmdb.dto.CIsDto
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.service.AliceTagManager
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ci.constants.CIConstants
import co.brainz.itsm.cmdb.ci.dto.CIComponentDataDto
import co.brainz.itsm.cmdb.ci.dto.CISearch
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
import co.brainz.itsm.cmdb.ci.entity.CIComponentDataEntity
import co.brainz.itsm.cmdb.ci.repository.CIComponentDataRepository
import co.brainz.itsm.cmdb.ciClass.service.CIClassService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.time.LocalDateTime
import kotlin.math.ceil
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CIService(
    private val ciService: CIService,
    private val ciClassService: CIClassService,
    private val ciComponentDataRepository: CIComponentDataRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val excelComponent: ExcelComponent,
    private val aliceTagManager: AliceTagManager,
    private val ciSearchService: CISearchService,
    private val ciRepository: CIRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
    private val listLinkedMapType: CollectionType =
        TypeFactory.defaultInstance().constructCollectionType(
            List::class.java,
            TypeFactory.defaultInstance().constructMapType(
                LinkedHashMap::class.java,
                String::class.java,
                Any::class.java
            )
        )

    /**
     * CMDB CI 단일 조회
     */
    fun getCI(ciId: String): CIDetailDto {
        return ciService.getCIDetail(ciId)
    }

    fun getCIsDummy(ciSearchCondition: CISearchCondition): CIDynamicReturnDto {
        val file = File("src/main/resources/public/assets/js/cmdb/dummy/ci.json")
        val params = mapper.readValue(file, Map::class.java)
        val ciData = CIDynamicListDto(
            searchItems = params["searchItems"] as MutableList<CISearchItem>,
            columnName = params["columnName"] as ArrayList<String>,
            columnTitle = params["columnTitle"] as ArrayList<String>,
            columnWidth = params["columnWidth"] as ArrayList<String>,
            columnType = params["columnType"] as ArrayList<String>,
            contents = mapper.convertValue(params["contents"],
                object : TypeReference<List<CIContentDto>>() {})
        )

        return CIDynamicReturnDto(
            data = ciData,
            paging = AlicePagingData(
                totalCount = 16L,
                totalCountWithoutCondition = 16L,
                currentPageNum = ciSearchCondition.pageNum,
                totalPageNum = ceil(16L.toDouble() / ciSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code
            )
        )
    }

    /**
     * CMDB CI 목록 조회
     */
    fun getCIs(ciSearchCondition: CISearchCondition): CIModalReturnDto {
        val result = ciService.getCIs(ciSearchCondition)
        return CIModalReturnDto(
            data = result.data,
            totalCount = result.paging.totalCount
        )
    }

    /**
     * CMDB CI 조회 (동적 컬럼)
     */
    fun getCIs(ciSearchCondition: CISearchCondition, searchItemsData: CISearch): CIDynamicReturnDto {
        ciSearchCondition.isPaging = false
        val dataList = ciRepository.findCIList(ciSearchCondition)
        val ciList: List<CIsDto> = mapper.convertValue(dataList.dataList, object : TypeReference<List<CIsDto>>() {})
        // 공통 출력 데이터 조회
        var basic = ciSearchService.getBasic(ciList)
        // 옵션 출력 데이터 조회
        val dynamic = ciSearchCondition.typeId?.let {
            ciSearchService.getDynamic(it, basic, searchItemsData, ciSearchCondition.isExcel)
        }
        // 공통 출력 + 옵션 출력 정보
        basic = ciSearchService.addDynamic(basic, dynamic)
        // 상세 검색 조건 필터링
        basic.contents = ciSearchService.getFilterContents(basic)
        // 코드 값을 실제 값으로 변경하는 작업
        basic.contents = ciSearchService.getConvertValue(basic)
        // 정렬
        basic.contents = ciSearchService.getOrderContents(basic, ciSearchCondition)
        val count = basic.contents.size.toLong()

        if (!ciSearchCondition.isExcel) {
            basic.contents = ciSearchService.getPaging(basic, ciSearchCondition)
        }

        return CIDynamicReturnDto(
            data = basic,
            paging = AlicePagingData(
                totalCount = count,
                totalCountWithoutCondition = ciRepository.count(),
                currentPageNum = ciSearchCondition.pageNum,
                totalPageNum = ceil(dataList.totalCount.toDouble() / ciSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.NAME_ASC.code,
                orderColName = ciSearchCondition.orderColName,
                orderDir = ciSearchCondition.orderDir
            )
        )
    }

    /**
     * CI 컴포넌트 - CI 데이터 조회
     */
    fun getCIData(parameter: LinkedHashMap<String, String>, modifyCIData: String): CIDetailDto {
        var ciDetailDto = CIDetailDto(
            ciId = parameter["ciId"] as String,
            interlink = parameter["interlink"]?.toBoolean()
        )
        // 전달된 데이터로 변경
        val map = mapper.readValue(modifyCIData, LinkedHashMap::class.java)
        val actionType = map["actionType"] as String
        if (actionType == CIConstants.ActionType.REGISTER.code || actionType == CIConstants.ActionType.MODIFY.code) {
            // 화면에서 전달된 데이터 세팅
            ciDetailDto.ciNo = map["ciNo"] as String
            ciDetailDto.ciName = map["ciName"] as String
            ciDetailDto.ciIcon = map["ciIcon"] as String
            ciDetailDto.ciIconData = map["ciIconData"] as String
            ciDetailDto.ciDesc = map["ciDesc"] as String
            ciDetailDto.ciStatus = map["ciStatus"] as String
            ciDetailDto.typeId = map["typeId"] as String
            ciDetailDto.typeName = map["typeName"] as String
            ciDetailDto.classId = map["classId"] as String

            val ciClassDetail = ciClassService.getCIClass(map["classId"] as String)
            ciDetailDto.className = ciClassDetail.className

            ciDetailDto.createUserKey = currentSessionUser.getUserKey()
            ciDetailDto.createDt = LocalDateTime.now()
            ciDetailDto.updateUserKey = currentSessionUser.getUserKey()
            ciDetailDto.updateDt = LocalDateTime.now()

            // CI 데이터 추출
            val ciClasses = ciClassService.getCIClassAttributes(
                parameter["ciId"] as String,
                map["classId"] as String
            )

            // 임시 테이블 데이터 조회
            val ciComponentData =
                ciComponentDataRepository.findByComponentIdAndCiIdAndInstanceId(
                    parameter["componentId"] as String,
                    parameter["ciId"] as String,
                    parameter["instanceId"] as String
                )
            if (ciComponentData != null) {
                val ciComponentDataValue: Map<String, Any> =
                    mapper.readValue(ciComponentData.values, object : TypeReference<Map<String, Any>>() {})
                // CI 세부 속성 변합
                this.mergeCIAttribute(
                    ciClasses,
                    mapper.convertValue(ciComponentDataValue["ciAttributes"], listLinkedMapType)
                )
            }
            ciDetailDto.classes = ciClasses
            ciDetailDto.ciTags =
                aliceTagManager.getTagsByTargetId(AliceTagConstants.TagType.CI.code, parameter["ciId"] as String)
        } else { // 삭제, 조회시 DB에 저장된 CI 데이터 조회
            ciDetailDto = getCI(parameter["ciId"] as String)
        }

        return ciDetailDto
    }

    /**
     * 임시 테이블의 CI 세부 속성 데이터가 존재할 경우, 데이터 병합
     */
    private fun mergeCIAttribute(
        ciClasses: List<CIClassDetailValueDto>,
        ciAttributesData: List<Map<String, Any>>
    ) {
        for (ciClass in ciClasses) {
            ciClass.attributes?.forEach { item ->
                run loop@{
                    ciAttributesData.forEach { data ->
                        if (data["id"] != null && data["value"] != null && item.attributeId == data["id"]) {
                            item.value = data["value"] as String
                            // Group List 속성일 경우, 하위 속성들의 값 할당
                            if (item.attributeType.equals(RestTemplateConstants.AttributeType.GROUP_LIST.code)) {
                                val ciAttributeDataList = this.mergeCIChildAttribute(
                                    mapper.convertValue(data["childAttributes"], listLinkedMapType),
                                    item
                                )
                                item.childAttributes = ciAttributeDataList
                            }
                            return@loop
                        }
                    }
                }
            }
        }
    }

    /**
     * 임시 테이블의 CI 세부 하위 속성 데이터가 존재할 경우, 데이터 병합
     */
    private fun mergeCIChildAttribute(
        childData: List<Map<String, Any>>,
        attribute: CIAttributeValueGroupListDto
    ): MutableList<CIAttributeValueDto> {
        val ciAttributeDataList = mutableListOf<CIAttributeValueDto>()
        childData.forEach { cData ->
            run cLoop@{
                attribute.childAttributes?.forEach { cItem ->
                    if (cData["id"] != null && cData["id"] == cItem.attributeId) {
                        ciAttributeDataList.add(
                            CIAttributeValueDto(
                                attributeId = cItem.attributeId,
                                attributeName = cItem.attributeName,
                                attributeDesc = cItem.attributeDesc,
                                attributeText = cItem.attributeText,
                                attributeType = cItem.attributeType,
                                attributeOrder = cData["seq"] as Int?,
                                attributeValue = cItem.attributeValue,
                                value = cData["value"] as String
                            )
                        )
                        return@cLoop
                    }
                }
            }
        }
        return ciAttributeDataList
    }

    /**
     * CI 컴포넌트 -  CI 세부 데이터 저장.
     */
    fun saveCIComponentData(ciId: String, ciComponentVO: RequestCIComponentVO): ZResponse {
        // 기존 CI 삭제
        val deleteCIComponentEntity = ciComponentDataRepository.findByCiIdAndComponentId(
            ciId, ciComponentVO.componentId
        )
        if (deleteCIComponentEntity != null) {
            ciComponentDataRepository.deleteByCiIdAndComponentId(
                ciId, ciComponentVO.componentId
            )
        }
        // CI 추가
        val ciComponentEntity = CIComponentDataEntity(
            ciId = ciId,
            componentId = ciComponentVO.componentId,
            values = mapper.writeValueAsString(ciComponentVO.values),
            instanceId = ciComponentVO.instanceId
        )
        ciComponentDataRepository.save(ciComponentEntity)

        return ZResponse()
    }

    /**
     * CI 컴포넌트 - CI 세부 데이터 삭제.
     */
    fun deleteCIComponentData(ciId: String, componentId: String): ZResponse {
        val ciComponentEntity = ciComponentDataRepository.findByCiIdAndComponentId(ciId, componentId)
        if (ciComponentEntity != null) {
            ciComponentDataRepository.deleteByCiIdAndComponentId(ciId, componentId)
        }
        return ZResponse()
    }

    /**
     * CMDB 히스토리 조회
     */
    fun getCIHistory(ciId: String): List<CIHistoryDto> {
        return ciService.getHistory(ciId)
    }

    /**
     * CI 연관 관계 정보 조회
     */
    fun getCIRelations(parameter: Any): Any? {
        var result: Any? = listOf<CIRelationDto>()
        // 타라미터의 타입에 따라 분기 처리 진행하며, LinkedHashMap 타입의 데이터를 전송한 경우,
        // wf_component_ci_data 테이블 데이터 유무에 따라 처리를 진행한다.
        // wf_component_ci_data 테이블에 연관관계 데이터가 존재하는 경우, 해당 데이터를 사용하고
        // 존재하지 않으면 cmdb_ci_relation 테이블에서 연관 관계 데이터를 가져온다.
        when (parameter is String) {
            true -> {
                result = ciService.getCIRelations(parameter)
            }
            false -> {
                if (parameter is LinkedHashMap<*, *>) {
                    val ciComponentData = ciComponentDataRepository.findByComponentIdAndCiIdAndInstanceId(
                        componentId = parameter["componentId"] as String,
                        ciId = parameter["ciId"] as String,
                        instanceId = parameter["instanceId"] as String
                    )
                    result = if (ciComponentData != null) {
                        val ciComponentDataValue: Map<String, Any> =
                            mapper.readValue(ciComponentData.values, object : TypeReference<Map<String, Any>>() {})
                        ciComponentDataValue["relatedCIData"]
                    } else {
                        val ciRelationData = ciService.getCIRelations(ciId = parameter["ciId"] as String)
                        ciRelationData
                    }
                }
            }
        }
        return result
    }

    /**
     * CI 컴포넌트 - CI 컴포넌트 세부 데이터 조회
     */
    fun getCIComponentData(ciId: String, componentId: String, instanceId: String): CIComponentDataDto? {
        val ciComponentData =
            ciComponentDataRepository.findByComponentIdAndCiIdAndInstanceId(componentId, ciId, instanceId)
        val relationList = mutableListOf<CIRelationDto>()

        if (ciComponentData != null) {
            val ciComponentDataValue: Map<String, Any> =
                mapper.readValue(ciComponentData.values, object : TypeReference<Map<String, Any>>() {})
            val relationData = mutableListOf<Map<String, String>>()
            relationData.addAll(mapper.convertValue(ciComponentDataValue["relatedCIData"], listLinkedMapType))
            relationData.forEach {
                relationList.add(mapper.convertValue(it, CIRelationDto::class.java))
            }
        }

        return if (ciComponentData != null) {
            CIComponentDataDto(
                ciId = ciComponentData.ciId,
                componentId = ciComponentData.componentId,
                values = ciComponentData.values,
                instanceId = ciComponentData.instanceId,
                ciRelations = relationList
            )
        } else {
            null
        }
    }

    /**
     * CI 조회 Excel 다운로드
     */
    fun getCIsExcelDownload(ciSearchCondition: CISearchCondition, searchItemsData: CISearch): ResponseEntity<ByteArray> {
        ciSearchCondition.isExcel = true
        val result = this.getCIs(ciSearchCondition, searchItemsData).data

        val titleRowVOList = mutableListOf<ExcelCellVO>()
        result?.columnTitle?.forEachIndexed { index, title ->
            when (result.columnType[index]) {
                CIAttributeConstants.Type.ICON.code,
                CIAttributeConstants.Type.HIDDEN.code -> {
                }
                else -> {
                    titleRowVOList.add(
                        ExcelCellVO(
                            value = title,
                            cellWidth = 5000
                        )
                    )
                }
            }
        }

        val excelVO = ExcelVO(
            sheets = mutableListOf(
                ExcelSheetVO(
                    rows = mutableListOf(
                        ExcelRowVO(
                            cells = titleRowVOList
                        )
                    )
                )
            )
        )

        val excelRowVOList = mutableListOf<ExcelRowVO>()
        result?.contents?.forEach { content ->
            val excelCellVOList = mutableListOf<ExcelCellVO>()
            content.value.forEachIndexed { index, value ->
                when (result.columnType[index]) {
                    CIAttributeConstants.Type.ICON.code,
                    CIAttributeConstants.Type.HIDDEN.code -> {
                    }
                    else -> {
                        excelCellVOList.add(ExcelCellVO(value = value))
                    }
                }
            }
            excelRowVOList.add(
                ExcelRowVO(
                    cells = excelCellVOList
                )
            )
        }
        excelVO.sheets[0].rows.addAll(excelRowVOList)
        return excelComponent.download(excelVO)
    }

    /**
     * CI 태그 목록을 String 변경
     */
    fun tagToString(tags: List<AliceTagDto>?): String {
        var tagStr = ""
        tags?.forEachIndexed { index, tag ->
            if (index > 0) {
                tagStr += ", "
            }
            tagStr += tag.tagValue
        }
        return tagStr
    }
}
