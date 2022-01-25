/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager.service

import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.dto.CICopyDataDto
import co.brainz.cmdb.dto.CIDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.fileTransaction.constants.FileConstants
import co.brainz.framework.fileTransaction.entity.AliceFileLocEntity
import co.brainz.framework.fileTransaction.entity.AliceFileOwnMapEntity
import co.brainz.framework.fileTransaction.repository.AliceFileLocRepository
import co.brainz.framework.fileTransaction.repository.AliceFileOwnMapRepository
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.service.NotificationService
import co.brainz.itsm.cmdb.ci.entity.CIComponentDataEntity
import co.brainz.itsm.cmdb.ci.repository.CIComponentDataRepository
import co.brainz.itsm.user.dto.UserAbsenceDto
import co.brainz.itsm.user.entity.UserCustomEntity
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.dto.WfTokenDataDto
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.instanceViewer.repository.WfInstanceViewerRepository
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class WfTokenManagerService(
    private val wfElementService: WfElementService,
    private val wfInstanceService: WfInstanceService,
    private val notificationService: NotificationService,
    private val documentRepository: WfDocumentRepository,
    private val wfElementRepository: WfElementRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val aliceUserRoleMapRepository: AliceUserRoleMapRepository,
    private val aliceFileService: AliceFileService,
    private val aliceUserRepository: AliceUserRepository,
    private val aliceFileLocRepository: AliceFileLocRepository,
    private val aliceFileOwnMapRepository: AliceFileOwnMapRepository,
    private val ciComponentDataRepository: CIComponentDataRepository,
    private val ciService: CIService,
    private val wfInstanceViewerRepository: WfInstanceViewerRepository
) {

    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Get component entity.
     */
    fun getComponent(componentId: String): WfComponentEntity {
        return wfComponentRepository.findById(componentId).get()
    }

    /**
     * Get element entity.
     */
    fun getElement(elementId: String): WfElementEntity {
        return wfElementRepository.findWfElementEntityByElementId(elementId)
    }

    /**
     * Get Instance entity.
     */
    fun getInstance(instanceId: String): WfInstanceEntity? {
        return wfInstanceRepository.findByInstanceId(instanceId)
    }

    /**
     * Get Document entity.
     */
    fun getDocument(documentId: String): WfDocumentEntity {
        return documentRepository.findDocumentEntityByDocumentId(documentId)
    }

    /**
     * Get component value(split[0]).
     */
    fun getComponentValue(tokenId: String, mappingId: String, componentValueType: String?): String {
        return if (componentValueType == WfComponentConstants.ComponentValueType.STRING_SEPARATOR.code) {
            wfTokenDataRepository.findWfTokenDataEntitiesByTokenTokenIdAndComponentComponentId(
                tokenId,
                mappingId
            ).value.split("|")[0]
        } else {
            wfTokenDataRepository.findWfTokenDataEntitiesByTokenTokenIdAndComponentComponentId(
                tokenId,
                mappingId
            ).value
        }
    }

    /**
     * Get component entity from componentIds and mappingId.
     */
    fun getComponentIdInAndMappingId(componentIds: List<String>, mappingId: String): WfComponentEntity {
        return wfComponentRepository.findByComponentIdInAndMappingId(componentIds, mappingId)
    }

    /**
     * Create instance.
     */
    fun createInstance(wfTokenDto: WfTokenDto): WfInstanceEntity {
        return wfInstanceService.createInstance(wfTokenDto)
    }

    /**
     * Complete instance.
     */
    fun completeInstance(instanceId: String) {
        return wfInstanceService.completeInstance(instanceId)
    }

    /**
     * Get start element.
     */
    fun getStartElement(processId: String): WfElementEntity {
        return wfElementService.getStartElement(processId)
    }

    /**
     * Get end element.
     */
    fun getEndElement(processId: String): WfElementEntity {
        return wfElementService.getEndElement(processId)
    }

    /**
     * Get next element.
     */
    fun getNextElement(wfTokenDto: WfTokenDto): WfElementEntity {
        return wfElementService.getNextElement(wfTokenDto)
    }

    /**
     * Get token.
     */
    fun getToken(tokenId: String): WfTokenEntity {
        return wfTokenRepository.findTokenEntityByTokenId(tokenId).get()
    }

    /**
     * 토큰 엔티티 저장.
     */
    fun saveToken(tokenEntity: WfTokenEntity): WfTokenEntity {
        return wfTokenRepository.save(tokenEntity)
    }

    /**
     * 첨부파일 업로드 경로(파일명 포함).
     */
    fun getUploadFilePath(fileName: String): Path {
        return aliceFileService.getUploadFilePath(FileConstants.Path.UPLOAD.path, fileName)
    }

    /**
     * 프로세스 파일 경로.
     */
    fun getProcessFilePath(attachFileName: String): Path {
        return Paths.get(FileConstants.Path.IMAGE.path + File.separator + attachFileName)
    }

    /**
     * Create CI.
     */
    fun createCI(ci: CIDto): String {
        val returnDto = ciService.createCI(ci)
        return returnDto.code
    }

    /**
     * Update CI.
     */
    fun updateCI(ci: CIDto): String {
        val returnDto = ciService.updateCI(ci)
        return returnDto.code
    }

    /**
     * Delete CI.
     */
    fun deleteCI(ci: CIDto): String {
        val returnDto = ciService.deleteCI(ci)
        return returnDto.code
    }

    /**
     * CI 임시 테이블 데이터 조회.
     */
    fun getComponentCIData(componentId: String, ciId: String, instanceId: String): CIComponentDataEntity? {
        return ciComponentDataRepository.findByComponentIdAndCiIdAndInstanceId(componentId, ciId, instanceId)
    }

    /**
     * CI 임시 테이블 목록 조회.
     */
    fun getComponentCiDataList(instanceId: String): List<CIComponentDataEntity>? {
        return ciComponentDataRepository.findByInstanceId(instanceId)
    }

    /**
     * CI 임시 테이블 삭제.
     */
    fun deleteCiComponentData(ciComponentDataEntities: List<CIComponentDataEntity>?) {
        ciComponentDataEntities?.forEach { data ->
            ciComponentDataRepository.deleteByCiIdAndComponentId(data.ciId, data.componentId)
        }
    }

    /**
     * 랜덤 파일명 생성.
     */
    fun getRandomFilename(): String {
        return aliceFileService.getRandomFilename()
    }

    /**
     * 파일 데이터 업로드.
     */
    fun uploadProcessFile(aliceFileLocEntity: AliceFileLocEntity): AliceFileLocEntity {
        val fileLocEntity = aliceFileLocRepository.save(aliceFileLocEntity)
        val fileOwnMapEntity = AliceFileOwnMapEntity(aliceFileLocEntity.fileOwner!!, fileLocEntity)
        aliceFileOwnMapRepository.save(fileOwnMapEntity)
        return fileLocEntity
    }

    /**
     * 사용자 정보 조회.
     */
    fun getUserInfo(assignee: String): AliceUserEntity {
        return aliceUserRepository.findByUserKey(assignee)
    }

    /**
     * 권한 위임 설정 정보 조회.
     */
    fun getAbsenceInfo(custom: UserCustomEntity): UserAbsenceDto {
        return mapper.readValue(custom.customValue, UserAbsenceDto::class.java)
    }

    /**
     * 토큰 DTO를 엔티티로 변환하여 저장.
     */
    fun saveToken(wfTokenDto: WfTokenDto): WfTokenEntity {
        val wfTokenEntity = WfTokenEntity(
            tokenId = "",
            tokenStatus = WfTokenConstants.Status.RUNNING.code,
            tokenStartDt = LocalDateTime.now(),
            instance = wfInstanceRepository.findByInstanceId(wfTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        )

        return this.saveToken(wfTokenEntity)
    }

    /**
     * 토큰이 속한 엘리먼트의 notification가 true인 경우, candidate 데이터와 assignee, 참조인을 대상으로 알림.
     */
    fun notificationCheck(token: WfTokenEntity) {
        if (!token.element.notification) {
            return
        }

        val notifications = mutableListOf<NotificationDto>()

        // Component Value
        val commonNotification = NotificationDto(
            title = token.instance.document.documentName,
            message = "[${token.element.elementName}] ${token.instance.document.documentDesc}",
            instanceId = token.instance.instanceId
        )

        token.assigneeId?.let {
            commonNotification.receivedUser = token.assigneeId!!
            notifications.add(commonNotification)
        }

        // Candidate Users, Candidate Groups
        var assigneeType = ""
        val assigneeValueList: MutableList<String> = mutableListOf()
        token.element.elementDataEntities.forEach { data ->
            if (data.attributeId == WfElementConstants.AttributeId.ASSIGNEE_TYPE.value) {
                assigneeType = data.attributeValue
            }
            if (data.attributeId == WfElementConstants.AttributeId.ASSIGNEE.value) {
                assigneeValueList.add(data.attributeValue)
            }
        }

        when (assigneeType) {
            WfTokenConstants.AssigneeType.USERS.code -> {
                for (assigneeValue in assigneeValueList) {
                    val notification = commonNotification.copy()
                    notification.receivedUser = assigneeValue
                    notifications.add(notification)
                }
            }
            WfTokenConstants.AssigneeType.GROUPS.code -> {
                val userRoleMapEntities = aliceUserRoleMapRepository.findUserRoleMapByRoleIds(assigneeValueList)
                for (userRoleMapEntity in userRoleMapEntities) {
                    val notification = commonNotification.copy()
                    notification.receivedUser = userRoleMapEntity.user.userKey
                    notifications.add(notification)
                }
            }
        }
        // 참조인 toast알림 발송
        // TODO : 참조인 알림 메일 발송
        val viewerEntities = wfInstanceViewerRepository.findViewerByInstanceId(token.instance.instanceId)

        if (viewerEntities.isNotEmpty()) {
            for (viewerEntity in viewerEntities) {
                val notification = commonNotification.copy()
                    notification.receivedUser = viewerEntity.viewer.userKey
                notifications.add(notification)
                wfInstanceViewerRepository.updateDisplayYn(token.instance.instanceId, viewerEntity.viewer.userKey)
            }
        }
        notificationService.insertNotificationList(notifications.distinct())
    }

    /**
     * Save all token data.
     */
    fun saveAllTokenData(tokenDataEntities: MutableList<WfTokenDataEntity>): MutableList<WfTokenDataEntity> {
        return wfTokenDataRepository.saveAll(tokenDataEntities)
    }

    /**
     * Make mapping tokenDto.
     * - MappingId가 존재하는 토큰데이터를 조회하여 TokenDto를 생성
     */
    fun makeMappingTokenDto(token: WfTokenEntity, documentId: List<String>): List<WfTokenDto> {
        val keyPairMappingIdAndTokenData = this.getTokenDataByMappingId(token)
        val tokensDto = mutableListOf<WfTokenDto>()
        documentId.forEach {
            val document = documentRepository.findDocumentEntityByDocumentId(it)
            val tokenDataList = mutableListOf<WfTokenDataDto>()
            document.form.components.forEach { component ->
                if (component.mappingId.isNotBlank() && keyPairMappingIdAndTokenData[component.mappingId] != null) {
                    val value = keyPairMappingIdAndTokenData[component.mappingId] as String
                    val data = WfTokenDataDto(componentId = component.componentId, value = value)
                    tokenDataList.add(data)
                }
            }
            tokensDto.add(
                WfTokenDto(
                    documentId = document.documentId,
                    data = tokenDataList,
                    action = WfElementConstants.Action.SAVE.value,
                    parentTokenId = token.tokenId
                )
            )
        }

        return tokensDto
    }

    /**
     * Make subProcess tokenDataDto.
     *  - SubProcess로 전달할 데이터 중에서 MappingId가 양쪽 문서에 연결된 데이터만 추출하여 Dto 생성
     */
    fun makeSubProcessTokenDataDto(
        subProcessToken: WfTokenEntity,
        mainProcessToken: WfTokenEntity
    ): List<WfTokenDataDto> {
        val keyPairMappingIdAndTokenData = this.getTokenDataByMappingId(subProcessToken)
        val componentIdAndTokenData = mutableMapOf<String, String>()
        mainProcessToken.instance.document.form.components.forEach {
            if (it.mappingId.isNotBlank() && keyPairMappingIdAndTokenData[it.mappingId] != null) {
                componentIdAndTokenData[it.componentId] = keyPairMappingIdAndTokenData[it.mappingId] as String
            }
        }
        val tokenData = mutableListOf<WfTokenDataDto>()
        mainProcessToken.tokenDataEntities.forEach {
            val componentId = it.component.componentId
            val componentValue = if (componentIdAndTokenData[componentId] != null) {
                componentIdAndTokenData[componentId] as String
            } else {
                it.value
            }
            tokenData.add(
                WfTokenDataDto(
                    componentId = componentId,
                    value = componentValue
                )
            )
        }

        return tokenData
    }

    /**
     * 상위 프로세스의 토큰아이디 [parentTokenId] 로 인스턴스를 조회 후 현재 진행중인 토큰의 처리담당자를 리턴한다
     */
    fun getCurrentAssigneeForChildProcess(parentTokenId: String): String? {
        val instance = wfInstanceRepository.findByPTokenId(parentTokenId) ?: return null
        val token = wfTokenRepository.findTopByInstanceOrderByTokenStartDtDesc(instance)
        return token?.assigneeId
    }

    /**
     * 서브 업무흐름에서 CI 저장과 관련된 서브 업무흐름이 존재하는 경우
     * wf_component_ci_data 테이블에 존재하는 메인 신청서와 관련된 CI 데이터에 대한 복사를 진행한다.
     */
    fun copyComponentCIData(
        startTokenDto: WfTokenDto,
        makeDocumentTokenDto: WfTokenDto
    ) {
        val document = documentRepository.findDocumentEntityByDocumentId(makeDocumentTokenDto.documentId!!)
        val mainCIComponentList = mutableListOf<WfComponentEntity>()
        val subCIComponentList = mutableListOf<CICopyDataDto>()
        val subCIElementList = mutableListOf<WfElementEntity>()
        // 메인 신청서의 문서양식의 컴포넌트 데이터 수집
        wfInstanceRepository.findByInstanceId(startTokenDto.instanceId)!!.document.form.components.forEach { component ->
            if (component.componentType == WfComponentConstants.ComponentTypeCode.CI.code && component.mappingId.isNotBlank()) {
                mainCIComponentList.add(component)
            }
        }

        // 서브 업무흐름의 문서양식으로 맵핑된 데이터 중에서 CI와 관련된 데이터 수집
        if (!makeDocumentTokenDto.data.isNullOrEmpty()) {
            makeDocumentTokenDto.data!!.forEach { wfTokenData ->
                val component = wfComponentRepository.findByComponentId(wfTokenData.componentId)
                if (component.componentType == WfComponentConstants.ComponentTypeCode.CI.code && component.mappingId.isNotBlank()) {
                    val data: Array<Map<String, String>> =
                        mapper.readValue(wfTokenData.value, object : TypeReference<Array<Map<String, String>>>() {})
                    data.forEach {
                        val ciCopyDataDto = CICopyDataDto(
                            ciId = it["ciId"] as String,
                            componentId = wfTokenData.componentId
                        )
                        subCIComponentList.add(ciCopyDataDto)
                    }
                }
            }
        }

        // 서브 업무흐름의 프로세스에서 CI와 관련된 엘리먼트 수집
        document.process.elementEntities.forEach { element ->
            element.elementDataEntities.forEach { elementData ->
                if (elementData.attributeId == WfElementConstants.AttributeId.SCRIPT_TYPE.value &&
                    elementData.attributeValue == WfElementConstants.ScriptType.DOCUMENT_CMDB.value
                ) {
                    subCIElementList.add(element)
                }
            }
        }

        // 서브 업무흐름의 문서양식이나 프로세스에서 CI 컴포넌트 및 엘리먼트가 존재하는 경우에만 CI 데이터에 대한 복사를 진행한다.
        if (subCIComponentList.isNotEmpty() && subCIElementList.isNotEmpty()) {
            mainCIComponentList.forEach { mainCIComponent ->
                subCIComponentList.forEach { subCIComponent ->
                    val mainData = ciComponentDataRepository.findByCiIdAndComponentId(
                        ciId = subCIComponent.ciId,
                        componentId = mainCIComponent.componentId
                    )
                    mainData?.let {
                        val copyData = ciComponentDataRepository.save(
                            CIComponentDataEntity(
                                ciId = subCIComponent.ciId,
                                values = mainData.values,
                                componentId = subCIComponent.componentId,
                                instanceId = makeDocumentTokenDto.instanceId
                            )
                        )

                        ciComponentDataRepository.save(copyData)
                    }
                }
            }
        }
    }

    /**
     * Get tokenData by mappingId.
     *  - 토큰에 포함된 컴포넌트 중 mappingId 값이 존재하는 목록을 조회한 후 Map으로 저장 (key: componentId, value: mappingId)
     *  - 토큰 데이터 중 위의 컴포넌트 목록에 포함된 데이터를 Map형태로 저장 (key: mappingId, value: tokenData value)
     */
    private fun getTokenDataByMappingId(token: WfTokenEntity): MutableMap<String, String> {
        val component = token.instance.document.form.components.filter {
            it.mappingId.isNotBlank()
        }
        val keyPairComponentIdToMappingId = component.associateBy({ it.componentId }, { it.mappingId })
        val keyPairMappingIdToTokenDataValue = mutableMapOf<String, String>()
        token.tokenDataEntities.forEach {
            val componentId = it.component.componentId
            if (keyPairComponentIdToMappingId.get(componentId) != null) {
                val mappingId = keyPairComponentIdToMappingId[componentId] as String
                keyPairMappingIdToTokenDataValue[mappingId] = it.value
            }
        }

        return keyPairMappingIdToTokenDataValue
    }
}
