/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.service

import co.brainz.api.dto.RequestCIComponentVO
import co.brainz.cmdb.ci.repository.CICapacityRepository
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
import co.brainz.itsm.cmdb.ci.dto.CICapacityChartDto
import co.brainz.itsm.cmdb.ci.dto.CIComponentDataDto
import co.brainz.itsm.cmdb.ci.dto.CIComponentInfo
import co.brainz.itsm.cmdb.ci.dto.CISearch
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
import co.brainz.itsm.cmdb.ci.entity.CIComponentDataEntity
import co.brainz.itsm.cmdb.ci.repository.CIComponentDataRepository
import co.brainz.itsm.cmdb.ciClass.service.CIClassService
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartData
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
    private val ciRepository: CIRepository,
    private val ciCapacityRepository: CICapacityRepository
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
     * CMDB CI ?????? ??????
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
     * CMDB CI ?????? ??????
     */
    fun getCIs(ciSearchCondition: CISearchCondition): CIModalReturnDto {
        val result = ciService.getCIs(ciSearchCondition)
        return CIModalReturnDto(
            data = result.data,
            totalCount = result.paging.totalCount
        )
    }

    /**
     * CMDB CI ?????? (?????? ??????)
     */
    fun getCIs(ciSearchCondition: CISearchCondition, searchItemsData: CISearch): CIDynamicReturnDto {
        ciSearchCondition.isPaging = false
        val dataList = ciRepository.findCIList(ciSearchCondition)
        val ciList: List<CIsDto> = mapper.convertValue(dataList.dataList, object : TypeReference<List<CIsDto>>() {})
        // ?????? ?????? ????????? ??????
        var basic = ciSearchService.getBasic(ciList)
        // ?????? ?????? ????????? ??????
        val dynamic = ciSearchCondition.typeId?.let {
            ciSearchService.getDynamic(it, basic, searchItemsData, ciSearchCondition.isExcel)
        }
        // ?????? ?????? + ?????? ?????? ??????
        basic = ciSearchService.addDynamic(basic, dynamic)
        // ?????? ?????? ?????? ?????????
        basic.contents = ciSearchService.getFilterContents(basic)
        // ?????? ?????? ?????? ????????? ???????????? ??????
        basic.contents = ciSearchService.getConvertValue(basic)
        // ??????
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
     * CI ???????????? - CI ????????? ??????
     */
    fun getCIData(parameter: CIComponentInfo, modifyCIData: String): CIDetailDto {
        var ciDetailDto = CIDetailDto(
            ciId = parameter.ciId,
            interlink = parameter.interlink.toBoolean()
        )
        // ????????? ???????????? ??????
        val map = mapper.readValue(modifyCIData, LinkedHashMap::class.java)
        val actionType = map["actionType"] as String
        if (actionType == CIConstants.ActionType.REGISTER.code || actionType == CIConstants.ActionType.MODIFY.code) {
            // ???????????? ????????? ????????? ??????
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

            // CI ????????? ??????
            val ciClasses = ciClassService.getCIClassAttributes(
                parameter.ciId,
                map["classId"] as String
            )

            // ?????? ????????? ????????? ??????
            val ciComponentData =
                ciComponentDataRepository.findByComponentIdAndCiIdAndInstanceId(
                    parameter.componentId,
                    parameter.ciId,
                    parameter.instanceId
                )
            if (ciComponentData != null) {
                val ciComponentDataValue: Map<String, Any> =
                    mapper.readValue(ciComponentData.values, object : TypeReference<Map<String, Any>>() {})
                // CI ?????? ?????? ??????
                this.mergeCIAttribute(
                    ciClasses,
                    mapper.convertValue(ciComponentDataValue["ciAttributes"], listLinkedMapType)
                )
            }
            ciDetailDto.classes = ciClasses
            ciDetailDto.ciTags =
                aliceTagManager.getTagsByTargetId(AliceTagConstants.TagType.CI.code, parameter.ciId)
        } else { // ??????, ????????? DB??? ????????? CI ????????? ??????
            ciDetailDto = getCI(parameter.ciId)
        }

        return ciDetailDto
    }

    /**
     * ?????? ???????????? CI ?????? ?????? ???????????? ????????? ??????, ????????? ??????
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
                            // Group List ????????? ??????, ?????? ???????????? ??? ??????
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
     * ?????? ???????????? CI ?????? ?????? ?????? ???????????? ????????? ??????, ????????? ??????
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
     * CI ???????????? -  CI ?????? ????????? ??????.
     */
    fun saveCIComponentData(ciId: String, ciComponentVO: RequestCIComponentVO): ZResponse {
        // ?????? CI ??????
        val deleteCIComponentEntity = ciComponentDataRepository.findByCiIdAndComponentId(
            ciId, ciComponentVO.componentId
        )
        if (deleteCIComponentEntity != null) {
            ciComponentDataRepository.deleteByCiIdAndComponentId(
                ciId, ciComponentVO.componentId
            )
        }
        // CI ??????
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
     * CI ???????????? - CI ?????? ????????? ??????.
     */
    fun deleteCIComponentData(ciId: String, componentId: String): ZResponse {
        val ciComponentEntity = ciComponentDataRepository.findByCiIdAndComponentId(ciId, componentId)
        if (ciComponentEntity != null) {
            ciComponentDataRepository.deleteByCiIdAndComponentId(ciId, componentId)
        }
        return ZResponse()
    }

    /**
     * CMDB ???????????? ??????
     */
    fun getCIHistory(ciId: String): List<CIHistoryDto> {
        return ciService.getHistory(ciId)
    }

    /**
     * CI ?????? ?????? ?????? ??????
     */
    fun getCIRelations(parameter: Any): Any? {
        var result: Any? = listOf<CIRelationDto>()
        // ??????????????? ????????? ?????? ?????? ?????? ????????????, LinkedHashMap ????????? ???????????? ????????? ??????,
        // wf_component_ci_data ????????? ????????? ????????? ?????? ????????? ????????????.
        // wf_component_ci_data ???????????? ???????????? ???????????? ???????????? ??????, ?????? ???????????? ????????????
        // ???????????? ????????? cmdb_ci_relation ??????????????? ?????? ?????? ???????????? ????????????.
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
     * CI ???????????? - CI ???????????? ?????? ????????? ??????
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
     * CI ?????? Excel ????????????
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
     * CI ?????? ????????? String ??????
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

    /**
     * CI ?????? ?????? ????????? ??????
     */
    fun getCapacityChartData(ciId: String): List<CICapacityChartDto>? {
        val capacityData = ciCapacityRepository.findCapacityChartData(ciId)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00")
        var from = LocalDateTime.now(ZoneId.of(currentSessionUser.getTimezone()))
        val memDataList = mutableListOf<ChartData>()
        val cpuDataList = mutableListOf<ChartData>()
        val diskDataList = mutableListOf<ChartData>()
        val returnDataList = mutableListOf<CICapacityChartDto>()
        if (capacityData.isNotEmpty()) {
            for (i in 1..CIConstants.period) {
                val formFormat = from.format(formatter)
                var cpuAvg = ""
                var memAvg = ""
                var diskAvg = ""

                loop@ for (capacity in capacityData) {
                    if (capacity.referenceDt.format(formatter) == formFormat) {
                        cpuAvg = capacity.cpuAvg.toString()
                        memAvg = capacity.memAvg.toString()
                        diskAvg = capacity.diskAvg.toString()
                        break@loop
                    }
                }
                memDataList.add(this.setChartData(formFormat, memAvg, CIConstants.CapacityTag.MEMORY.code))
                cpuDataList.add(this.setChartData(formFormat, cpuAvg, CIConstants.CapacityTag.CPU.code))
                diskDataList.add(this.setChartData(formFormat, diskAvg, CIConstants.CapacityTag.DISK.code))
                from = from.minusHours(1)
            }

            returnDataList.add(this.setCapacityChartData(ciId, memDataList, CIConstants.CapacityTag.MEMORY.code))
            returnDataList.add(this.setCapacityChartData(ciId, cpuDataList, CIConstants.CapacityTag.CPU.code))
            returnDataList.add(this.setCapacityChartData(ciId, diskDataList, CIConstants.CapacityTag.DISK.code))
        }
        return returnDataList
    }

    /**
     * ?????? ????????? Setting
     */
    private fun setChartData(category: String, chartValue: String, series: String): ChartData {
        return ChartData(
            id = "",
            category = category,
            value = chartValue,
            series = series
        )
    }

    /**
     * ????????? ?????? ??? ?????? ?????? ????????? Setting
     */
    private fun setCapacityChartData(ciId: String, dataList: MutableList<ChartData>, tag: String): CICapacityChartDto {
        return CICapacityChartDto(
            chartId = ciId,
            chartType = ChartConstants.Type.BASIC_LINE.code,
            tags = mutableListOf(
                AliceTagDto(tagValue = tag)
            ),
            chartConfig = this.initChartConfig(),
            chartData = dataList
        )
    }

    /**
     * ?????? ?????? Setting
     */
    private fun initChartConfig(): ChartConfig {
        val range = ChartRange(
            type = ChartConstants.Range.BETWEEN.code,
            fromDate = LocalDate.now().minusDays(7L),
            toDate = LocalDate.now()
        )
        return ChartConfig(
            range = range,
            operation = ChartConstants.Operation.PERCENT.code,
            periodUnit = ChartConstants.Unit.HOUR.code
        )
    }
}
