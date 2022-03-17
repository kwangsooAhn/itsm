/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.service

import co.brainz.cmdb.ci.entity.CIDataEntity
import co.brainz.cmdb.ci.entity.CIEntity
import co.brainz.cmdb.ci.entity.CIHistoryEntity
import co.brainz.cmdb.ci.repository.CIDataRepository
import co.brainz.cmdb.ci.repository.CIHistoryRepository
import co.brainz.cmdb.ci.repository.CIRepository
import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.ciAttribute.repository.CIAttributeRepository
import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIClassToAttributeDto
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ci.constants.CITemplateConstants
import co.brainz.itsm.cmdb.ciClass.service.CIClassService
import co.brainz.itsm.cmdb.ciType.service.CITypeService
import java.time.LocalDateTime
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class CITemplateService(
    private val ciClassService: CIClassService,
    private val excelComponent: ExcelComponent,
    private val ciTypeService: CITypeService,
    private val ciService: CIService,
    private val ciRepository: CIRepository,
    private val ciDataRepository: CIDataRepository,
    private val ciAttributeRepository: CIAttributeRepository,
    private val ciHistoryRepository: CIHistoryRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val aliceUserRepository: AliceUserRepository

) {

    /**
     * CI 일괄 등록 템플릿 다운로드.
     *
     * @param typeId
     * @return ResponseEntity<ByteArray>
     */
    fun getCIsTemplateDownload(typeId: String): ResponseEntity<ByteArray> {
        val classId = ciTypeService.getCIType(typeId).classId
        val titleRowVOList = mutableListOf<ExcelCellVO>()
        val allAttributeList = this.getTemplateAttributeList(classId)

        this.templateColumnTitle().forEach {
            titleRowVOList.add(
                ExcelCellVO(
                    value = it,
                    cellWidth = 5000
                )
            )
        }

        allAttributeList.let {
            allAttributeList.forEach {
                titleRowVOList.add(
                    ExcelCellVO(
                        value = it.attributeName,
                        cellWidth = 5000
                    )
                )
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
        return excelComponent.download(excelVO)
    }

    /**
     * CI 일괄 등록 템플릿 타이틀 생성.
     *
     * @return ArrayList<String>
     */
    private fun templateColumnTitle(): ArrayList<String> {
        val columnTitleList = arrayListOf<String>()
        for (property in CITemplateConstants.BasicProperties.values()) {
            columnTitleList.add(property.value)
        }

        return columnTitleList
    }

    /**
     * Attribute 데이터 조회.
     *
     * @param classId
     * @return MutableList<CIClassToAttributeDto>
     */
    private fun getTemplateAttributeList(classId: String): MutableList<CIClassToAttributeDto> {
        val attributeList = ciClassService.getCIClass(classId).attributes
        val extendsAttributeList = ciClassService.getCIClass(classId).extendsAttributes
        var allAttributeList: MutableList<CIClassToAttributeDto> = mutableListOf()

        // 기존 속성항목 추가
        attributeList?.let {
            allAttributeList.addAll(attributeList)
        }
        // 부모 속성항목 추가
        extendsAttributeList?.let {
            allAttributeList.addAll(extendsAttributeList)
        }

        // 정렬 진행 ( classLevel -> order 순으로 정렬 )
        allAttributeList = allAttributeList.sortedWith(
            compareBy({ it.classLevel }, { it.order })
        ).toMutableList()

        return allAttributeList
    }


    /**
     * CI 일괄 등록 템플릿 업로드.
     *mappingDataMap
     * @param files
     * @return MutableList<CIClassToAttributeDto>
     */
    fun uploadCIsTemplate(files: MultipartFile): String {
        try {
            val workBook = XSSFWorkbook(OPCPackage.open(files.inputStream))
            val sheet = workBook.getSheetAt(0)

            // 1.  sheet에서 typeName 조회 및 CI TYPE Entity 조회
            val typeName = this.getCITypeNameDataFromSheet(sheet)
            val ciTypeEntity = ciTypeService.getCITypeByTypeName(typeName)
                ?: throw AliceException(
                    AliceErrorConstants.ERR_00005,
                    AliceErrorConstants.ERR_00005.message + "[CI Type Entity]"
                )


            // 2. type_name 과 관련된 Attribute 전체 조회 (부모 + 자신)
            val allAttributeList = if (typeName.isNotBlank()) {
                this.getAllAttributeList(typeName)
            } else {
                throw AliceException(
                    AliceErrorConstants.ERR_00005,
                    AliceErrorConstants.ERR_00005.message + "[Template Attribute List]"
                )
            }

            // 3. 변환된 templateData 및 attributeData간의 맵핑 진행 (attribute_name간 맵핑 진행)
            val mappingDataList = if (allAttributeList.isNotEmpty()) {
                this.getMappingMapData(
                    this.getTemplateDataList(sheet),
                    this.getAttributeDataList(allAttributeList)
                )
            } else {
                throw AliceException(
                    AliceErrorConstants.ERR_00005,
                    AliceErrorConstants.ERR_00005.message + "[Template MappingData List]"
                )
            }

            // 4. cmdb_ci 데이터 등록
            mappingDataList.forEach { mappingDataMap ->
                val uuid = AliceUtil().getUUID()

                // 4-1. cmdb_ci 데이터 등록
                val ciEntity = CIEntity(
                    ciId = uuid,
                    ciNo = ciService.getCINo(ciTypeEntity),
                    ciName = mappingDataMap[0][CITemplateConstants.BasicProperties.CI_NAME.value].toString(),
                    ciStatus = RestTemplateConstants.CIStatus.STATUS_USE.code,
                    ciTypeEntity = ciTypeEntity,
                    ciDesc = mappingDataMap[0][CITemplateConstants.BasicProperties.CI_DESC.value].toString(),
                    interlink = false,
                    createDt = LocalDateTime.now(),
                    createUser = aliceUserRepository.findAliceUserEntityByUserKey(currentSessionUser.getUserKey())
                )
                ciRepository.save(ciEntity)

                // 4-2. cmdb_ci_data 데이터 등록
                for (index in 1 until mappingDataMap.size) {
                    ciDataRepository.save(
                        CIDataEntity(
                            ci = ciEntity,
                            ciAttribute = ciAttributeRepository.getOne(mappingDataMap[index]["attributeId"].toString()),
                            value = mappingDataMap[index]["attributeValue"].toString()
                        )
                    )

                }

                // 4-3. cmdb_ci_group_list_data 데이터 등록

                // 4-4. cmdb_ci_history 데이터 등록
                val ciHistoryEntity = CIHistoryEntity(
                    historyId = "",
                    seq = 0,
                    ciId = ciEntity.ciId,
                    ciNo = ciEntity.ciNo,
                    ciName = ciEntity.ciName,
                    ciDesc = ciEntity.ciDesc,
                    typeId = ciEntity.ciTypeEntity.typeId,
                    ciIcon = ciEntity.ciTypeEntity.typeIcon,
                    ciStatus = ciEntity.ciStatus,
                    classId = ciEntity.ciTypeEntity.ciClass.classId,
                    interlink = ciEntity.interlink,
                    instance = ciEntity.instance,
                    applyDt = ciEntity.updateDt
                )
                ciHistoryRepository.save(ciHistoryEntity)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    /**
     * CI Type Name과 관련된 Attribute 조회 (부모 + 자식)
     *
     * @param typeName
     * @return MutableList<CIClassToAttributeDto>
     */
    private fun getAllAttributeList(typeName: String): MutableList<CIClassToAttributeDto> {
        var allAttributeList: MutableList<CIClassToAttributeDto> = mutableListOf()

        // CI Attribute 검색
        ciTypeService.getCITypeByTypeName(typeName)?.let { ciTypeEntity ->
            allAttributeList = this.getTemplateAttributeList(ciTypeEntity.ciClass.classId)
        }

        return allAttributeList
    }

    /**
     * CI 일괄 등록 템플릿 입력 데이터 변환.
     *
     * @param sheet
     * @return MutableList<LinkedHashMap<String, String>>
     */
    private fun getTemplateDataList(sheet: XSSFSheet): MutableList<LinkedHashMap<String, String>> {
        val mapList: MutableList<LinkedHashMap<String, String>> = mutableListOf()
        val firstRowCellValueList = mutableListOf<String>()
        var firstRowLastCellNum: Short = 0

        sheet.forEachIndexed { index, row ->
            when (index) {
                0 -> {
                    firstRowLastCellNum = row.lastCellNum
                    for (cellNum in 0 until firstRowLastCellNum) {
                        val value = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                        firstRowCellValueList.add(value)
                    }
                }
                else -> {
                    val map: LinkedHashMap<String, String> = linkedMapOf()
                    for (cellNum in 0 until firstRowLastCellNum) {
                        val value = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString()
                        map[firstRowCellValueList[cellNum]] = value
                    }
                    mapList.add(map)
                }
            }
        }

        return mapList
    }

    /**
     * AttributeList 데이터 변환.
     *
     * @param attributeList
     * @return MutableList<LinkedHashMap<String, String>>
     */
    private fun getAttributeDataList(attributeList: MutableList<CIClassToAttributeDto>): MutableList<LinkedHashMap<String, String>> {
        val mapList: MutableList<LinkedHashMap<String, String>> = mutableListOf()
        attributeList.forEach { ciClassToAttributeDto ->
            val map = LinkedHashMap<String, String>()
            ciClassToAttributeDto.attributeId?.let { map["attributeId"] = it }
            ciClassToAttributeDto.attributeName?.let { map["attributeName"] = it }
            ciClassToAttributeDto.attributeType?.let { map["attributeType"] = it }
            mapList.add(map)
        }

        return mapList
    }

    /**
     * 변환된 attributeDataMap과 templateDataMap간의 맵핑을 진행한다.
     * templateDataList에서 key(attributeName)와 attributeDataList의 attributeName 간의 맵핑을 진행한다
     *
     * @param templateDataList
     * @param attributeDataList
     *
     * @Return MutableList<MutableList<LinkedHashMap<String, String>>>
     */
    private fun getMappingMapData(
        templateDataList: MutableList<LinkedHashMap<String, String>>,
        attributeDataList: MutableList<LinkedHashMap<String, String>>
    ): MutableList<MutableList<LinkedHashMap<String, String>>> {
        val returnList: MutableList<MutableList<LinkedHashMap<String, String>>> = mutableListOf()

        for (i in 0 until templateDataList.size) {
            val templateData = templateDataList[i]
            val mapList: MutableList<LinkedHashMap<String, String>> = mutableListOf()
            val ciPropertiesMap = LinkedHashMap<String, String>()

            // CI 기본 속성 추가
            for (property in CITemplateConstants.BasicProperties.values()) {
                if (templateData[property.value] != null) {
                    ciPropertiesMap[property.value] = templateData[property.value].toString()
                }
            }
            mapList.add(ciPropertiesMap)

            // templateDataList, attributeDataList의 Data Mapping
            attributeDataList.forEach { attributeMap ->
                val map = LinkedHashMap<String, String>()
                if (templateData[attributeMap["attributeName"]] != null) {
                    map["attributeId"] = attributeMap["attributeId"].toString()
                    map["attributeName"] = attributeMap["attributeName"].toString()
                    map["attributeType"] = attributeMap["attributeType"].toString()
                    map["attributeValue"] = templateData[attributeMap["attributeName"]].toString()
                }
                mapList.add(map)
            }
            returnList.add(mapList)
        }

        return returnList
    }

    /**
     * Sheet 내부 typeName 데이터 수집. (CI 유형 컬럼에서 가장 첫 번째 데이터 수집)
     *
     * @param sheet
     * @return String?
     */
    private fun getCITypeNameDataFromSheet(sheet: XSSFSheet): String {
        var targetCellNum = 0
        var typeName = ""
        run {
            sheet.forEachIndexed { index, row ->
                when (index) {
                    0 -> {
                        for (cellNum in 0 until row.lastCellNum) {
                            if (row.getCell(cellNum).toString() == CITemplateConstants.BasicProperties.TYPE_NAME.value
                            ) {
                                targetCellNum = cellNum
                            }
                        }
                    }
                    else -> {
                        if (row.getCell(targetCellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
                                .toString() != ""
                        ) {
                            typeName = row.getCell(targetCellNum).toString()
                            return@run
                        }
                    }
                }
            }
        }

        return typeName
    }
}
