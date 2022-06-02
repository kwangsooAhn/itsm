/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.token.service

import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.fileTransaction.provider.AliceFileProvider
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.document.service.DocumentActionService
import co.brainz.itsm.process.dto.TokenStatusDto
import co.brainz.itsm.token.dto.TokenSearchCondition
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.dto.WfCIComponentValueDto
import co.brainz.workflow.component.service.WfComponentService
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.constants.WorkflowConstants
import co.brainz.workflow.provider.dto.RestTemplateInstanceExcelDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListReturnDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.service.WfTokenService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

@Service
class TokenService(
    private val aliceFileProvider: AliceFileProvider,
    private val documentActionService: DocumentActionService,
    private val wfInstanceService: WfInstanceService,
    private val wfTokenService: WfTokenService,
    private val wfComponentService: WfComponentService,
    private val aliceFileService: AliceFileService,
    private val currentSessionUser: CurrentSessionUser,
    private val wfEngine: WfEngine,
    private val aliceMessageSource: AliceMessageSource,
    private val excelComponent: ExcelComponent,
    private val ciTypeService: CITypeService
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Post Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Boolean
     */
    fun postToken(restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): ZResponse {
        restTemplateTokenDataUpdateDto.assigneeId = currentSessionUser.getUserKey()

        val tokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            instanceId = restTemplateTokenDataUpdateDto.instanceId,
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>,
            instanceCreateUser = restTemplateTokenDataUpdateDto.assigneeId?.let { AliceUserEntity(userKey = it) },
            action = restTemplateTokenDataUpdateDto.action,
            instancePlatform = WorkflowConstants.InstancePlatform.ITSM.code
        )

        // 특정 컴포넌트의 데이터 셋팅 (CI)
        if (restTemplateTokenDataUpdateDto.componentData != null) {
            this.componentDataConverter(restTemplateTokenDataUpdateDto.componentData!!)
        }

        restTemplateTokenDataUpdateDto.componentData!!.forEach {
            when (this.isFileUploadComponent(it.componentId) && it.value.isNotEmpty()) {
                true -> this.aliceFileService.uploadFiles(it.value)
            }
        }
        val isSuccess = wfEngine.startWorkflow(wfEngine.toTokenDto(tokenDto))
        return ZResponse(
            status = if (isSuccess) ZResponseConstants.STATUS.SUCCESS.code else ZResponseConstants.STATUS.ERROR_FAIL.code
        )
    }

    /**
     * CI 컴포넌트의 경우 데이터를 셋팅한다.
     *  화면에서 받은 데이터 중
     *  1) 값이 없는 경우 DTO 기본값 셋팅
     *  2) 필수 값: ciId, typeId, ciName, ciDesc, actionType
     */
    fun componentDataConverter(componentData: List<RestTemplateTokenDataDto>): List<RestTemplateTokenDataDto> {
        val components = wfComponentService.getComponents(componentData.map(RestTemplateTokenDataDto::componentId).toSet())

        componentData.forEach { data ->
            if (data.value.isEmpty()) { return@forEach }
            val component = components.first { it.componentId == data.componentId }
            when (component.componentType) {
                WfComponentConstants.ComponentType.CI.code -> {
                    if (data.value.isEmpty()) { return@forEach }
                    val dataValueList: List<WfCIComponentValueDto> =
                        mapper.readValue(data.value,
                            TypeFactory.defaultInstance().constructCollectionType(List::class.java, WfCIComponentValueDto::class.java))

                    dataValueList.forEach { dataValue ->
                        val ciType = ciTypeService.getCIType(dataValue.typeId)
                        dataValue.typeName = ciType?.typeName
                        dataValue.classId = ciType?.classId
                        dataValue.ciIcon = ciType?.typeIcon
                        dataValue.ciIconData = ciType?.typeIcon?.let { ciTypeService.getCITypeImageData(it) }
                    }
                    data.value = mapper.writeValueAsString(dataValueList)
                }
            }
        }
        return componentData
    }

    /**
     * Put Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     */
    fun putToken(tokenId: String, restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): ZResponse {
        restTemplateTokenDataUpdateDto.assigneeId = currentSessionUser.getUserKey()

        val tokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            instanceId = restTemplateTokenDataUpdateDto.instanceId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>,
            action = restTemplateTokenDataUpdateDto.action,
            instancePlatform = WorkflowConstants.InstancePlatform.ITSM.code
        )

        restTemplateTokenDataUpdateDto.componentData!!.forEach {
            when (this.isFileUploadComponent(it.componentId) && it.value.isNotEmpty()) {
                true -> this.aliceFileService.uploadFiles(it.value)
            }
        }
        val isSuccess = wfEngine.progressWorkflow(wfEngine.toTokenDto(tokenDto))
        return ZResponse(
            status = if (isSuccess) ZResponseConstants.STATUS.SUCCESS.code else ZResponseConstants.STATUS.ERROR_FAIL.code
        )
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @param tokenSearchCondition
     * @return List<tokenDto>
     */
    fun getTokenList(
        tokenSearchCondition: TokenSearchCondition
    ): RestTemplateInstanceListReturnDto {
        tokenSearchCondition.userKey = currentSessionUser.getUserKey()
        return wfInstanceService.instances(tokenSearchCondition)
    }

    /**
     * [tokenId]를 받아서 처리할 문서 상세 조회 하여 [String]반환 한다.
     */
    fun findToken(tokenId: String): String {
        return documentActionService.makeTokenAction(
            mapper.writeValueAsString(wfTokenService.getTokenData(tokenId))
        )
    }

    /**
     * 처리할 문서 카운트 (메뉴 - 문서함)
     */
    fun getTodoTokenCount(): Long = wfInstanceService.getInstanceTodoCount()

    /**
     * 해당 인스턴스를 가진 토큰 데이터를 조회한다.
     */
    fun findTokenByInstanceId(instanceId: String): List<WfTokenDto> {
        return wfTokenService.findTokenByInstanceId(instanceId)
    }

    /**
     * 문서함 Excel List 조회
     */
    private fun getTokenListForExcel(
        tokenSearchCondition: TokenSearchCondition
    ): MutableList<RestTemplateInstanceExcelDto> {
        tokenSearchCondition.userKey = currentSessionUser.getUserKey()
        return wfInstanceService.instancesForExcel(tokenSearchCondition)
    }

    /**
     * 컴포넌트 파일업로드 판단
     */
    private fun isFileUploadComponent(componentId: String): Boolean {
        return wfComponentService.getComponentTypeById(componentId) == WfComponentConstants.ComponentType.FILEUPLOAD.code
    }

    /**
     * 문서함 Excel 다운로드
     */
    fun getTokensExcelDownload(tokenSearchCondition: TokenSearchCondition): ResponseEntity<ByteArray> {
        val returnDto = getTokenListForExcel(tokenSearchCondition)
        val excelVO = ExcelVO(
            sheets = mutableListOf(
                ExcelSheetVO(
                    rows = mutableListOf(
                        ExcelRowVO(
                            cells = listOf(
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("token.excel.docNo"),
                                    cellWidth = 6000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("token.excel.name"),
                                    cellWidth = 7000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("token.excel.type"),
                                    cellWidth = 8000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("token.excel.assignee"),
                                    cellWidth = 3000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("token.excel.status"),
                                    cellWidth = 3000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("token.excel.createUser"),
                                    cellWidth = 6000
                                ),
                                ExcelCellVO(
                                    value = aliceMessageSource.getMessage("token.excel.createDt"),
                                    cellWidth = 6000
                                )
                            )
                        )
                    )
                )
            )
        )

        returnDto.forEachIndexed { index, result ->
            excelVO.sheets[0].rows.add(
                ExcelRowVO(
                    cells = mutableListOf(
                        ExcelCellVO(value = result.documentNo),
                        ExcelCellVO(value = result.topics?.get(index).toString()),
                        ExcelCellVO(value = result.documentName),
                        ExcelCellVO(value = if (result.assigneeUserName != null) result.assigneeUserName else ""),
                        ExcelCellVO(value = result.elementName),
                        ExcelCellVO(value = result.instanceCreateUser),
                        ExcelCellVO(value = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                            .format(AliceUtil().changeTimeBasedTimezone(result.instanceStartDt, currentSessionUser.getTimezone()))
                        )
                    )
                )
            )
        }
        return excelComponent.download(excelVO)
    }

    /**
     * 프로세스 맵.
     */
    fun getTokenStatus(instanceId: String): TokenStatusDto {
        val resultString = Gson().toJson(wfInstanceService.getInstanceLatestToken(instanceId))
        val tokenStatusDto = Gson().fromJson(resultString, TokenStatusDto::class.java)
        val xmlFile = aliceFileProvider.getProcessStatusFile(tokenStatusDto.processId)
        if (xmlFile.exists()) {
            val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile)
            xmlDoc.documentElement.normalize()

            val imageNodeList: NodeList = xmlDoc.getElementsByTagName("image")
            if (imageNodeList.length > 0) {
                val imageNode: Node = imageNodeList.item(0)
                if (imageNode.nodeType == Node.ELEMENT_NODE) {
                    val element = imageNode as Element
                    for (i in 0 until element.attributes.length) {
                        val nodeValue = element.attributes.item(i).nodeValue
                        when (element.attributes.item(i).nodeName) {
                            "left" -> tokenStatusDto.left = nodeValue
                            "top" -> tokenStatusDto.top = nodeValue
                            "width" -> tokenStatusDto.width = nodeValue
                            "height" -> tokenStatusDto.height = nodeValue
                        }
                    }
                    tokenStatusDto.imageData = element.textContent
                }

                val elementList = mutableListOf<LinkedHashMap<String, String>>()
                val elementNodeList: NodeList = xmlDoc.getElementsByTagName("element")
                for (i in 0 until elementNodeList.length) {
                    val elementNode: Node = elementNodeList.item(i)
                    if (elementNode.nodeType == Node.ELEMENT_NODE) {
                        val element = elementNode as Element
                        val elementMap = LinkedHashMap<String, String>()
                        for (j in 0 until element.attributes.length) {
                            elementMap[element.attributes.item(j).nodeName] = element.attributes.item(j).nodeValue
                        }
                        elementList.add(elementMap)
                    }
                }
                tokenStatusDto.elements = elementList
            }
        }
        return tokenStatusDto
    }
}
