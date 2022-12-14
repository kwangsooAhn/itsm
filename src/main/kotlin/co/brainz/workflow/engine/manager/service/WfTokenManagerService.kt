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
import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.service.NotificationService
import co.brainz.framework.resourceManager.constants.ResourceConstants
import co.brainz.framework.resourceManager.entity.AliceFileLocEntity
import co.brainz.framework.resourceManager.entity.AliceFileOwnMapEntity
import co.brainz.framework.resourceManager.repository.AliceFileLocRepository
import co.brainz.framework.resourceManager.repository.AliceFileOwnMapRepository
import co.brainz.framework.util.AliceResourceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ci.entity.CIComponentDataEntity
import co.brainz.itsm.cmdb.ci.repository.CIComponentDataRepository
import co.brainz.itsm.instance.repository.ViewerRepository
import co.brainz.itsm.plugin.service.PluginService
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
import org.springframework.core.env.Environment
import org.springframework.data.repository.findByIdOrNull
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
    private val aliceUserRepository: AliceUserRepository,
    private val aliceFileLocRepository: AliceFileLocRepository,
    private val aliceFileOwnMapRepository: AliceFileOwnMapRepository,
    private val ciComponentDataRepository: CIComponentDataRepository,
    private val ciService: CIService,
    private val viewerRepository: ViewerRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val pluginService: PluginService,
    environment: Environment
) : AliceResourceUtil(environment) {

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
        var value = ""
        val tokenData = wfTokenDataRepository
            .findWfTokenDataEntitiesByTokenTokenIdAndComponentComponentId(tokenId, mappingId)
        if (tokenData != null && tokenData.value != WfComponentConstants.DEFAULT_VALUE) {
            value = if (componentValueType == WfComponentConstants.ComponentValueType.STRING_SEPARATOR.code) {
                tokenData.value.split("|")[0]
            } else {
                tokenData.value
            }
        }

        return value
    }

    /**
     * Get component entity from componentIds and mappingId.
     */
    fun getComponentIdInAndMappingId(componentIds: List<String>, mappingId: String): WfComponentEntity {
        return wfComponentRepository.findByComponentIdInAndMappingId(componentIds, mappingId)
    }

    /**
     * Get component entities from componentIds and componentType.
     */
    fun getComponentList(componentIds: Set<String>, componentType: String): List<WfComponentEntity> {
        return wfComponentRepository.findByComponentIdsAndComponentType(componentIds, componentType)
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
     * ?????? ????????? ??????.
     */
    fun saveToken(tokenEntity: WfTokenEntity): WfTokenEntity {
        return wfTokenRepository.save(tokenEntity)
    }

    /**
     * Delete token By instanceId.
     */
    fun deleteTokenByInstanceId(instanceId: String) {
        val instance = wfInstanceRepository.findByInstanceId(instanceId)
        if (instance != null) {
            wfTokenRepository.deleteWfTokenEntityByInstance(instance)
        }
    }

    /**
     * ???????????? ?????? ??????.
     */
    fun getProcessFilePath(attachFileName: String): Path {
        return Paths.get(super.getPath(ResourceConstants.Path.FILE.path).toString() + File.separator + attachFileName)
    }

    /**
     * ???????????? ??????
     */
    fun executePlugin(pluginId: String, tokenDto: WfTokenDto, param: LinkedHashMap<String, Any>) {
        pluginService.executePlugin(pluginId, tokenDto, null, param)
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
     * CI ?????? ????????? ????????? ??????.
     */
    fun getComponentCIData(componentId: String, ciId: String, instanceId: String): CIComponentDataEntity? {
        return ciComponentDataRepository.findByComponentIdAndCiIdAndInstanceId(componentId, ciId, instanceId)
    }

    /**
     * CI ?????? ????????? ?????? ??????.
     */
    fun getComponentCiDataList(instanceId: String): List<CIComponentDataEntity>? {
        return ciComponentDataRepository.findByInstanceId(instanceId)
    }

    /**
     * CI ?????? ????????? ??????.
     */
    fun deleteCiComponentData(ciComponentDataEntities: List<CIComponentDataEntity>?) {
        ciComponentDataEntities?.forEach { data ->
            ciComponentDataRepository.deleteByCiIdAndComponentId(data.ciId, data.componentId)
        }
    }

    /**
     * ?????? ????????? ?????????.
     */
    fun uploadProcessFile(aliceFileLocEntity: AliceFileLocEntity): AliceFileLocEntity {
        val fileLocEntity = aliceFileLocRepository.save(aliceFileLocEntity)
        val fileOwnMapEntity = AliceFileOwnMapEntity(aliceFileLocEntity.fileOwner!!, fileLocEntity)
        aliceFileOwnMapRepository.save(fileOwnMapEntity)
        return fileLocEntity
    }

    /**
     * ????????? ?????? ??????.
     */
    fun getUserInfo(assignee: String): AliceUserEntity? {
        return aliceUserRepository.findByIdOrNull(assignee)
    }

    /**
     * ?????? ?????? ?????? ?????? ??????.
     */
    fun getAbsenceInfo(custom: UserCustomEntity): UserAbsenceDto {
        return mapper.readValue(custom.customValue, UserAbsenceDto::class.java)
    }

    /**
     * ?????? DTO??? ???????????? ???????????? ??????.
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
     * ????????? ?????? ??????????????? notification??? true??? ??????, candidate ???????????? assignee, ???????????? ???????????? ??????.
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
        // ????????? toast?????? ??????
        val viewerEntities = viewerRepository.findViewerByInstanceId(token.instance.instanceId)
        if (viewerEntities.isNotEmpty()) {
            for (viewerEntity in viewerEntities) {
                val notification = commonNotification.copy()
                notification.receivedUser = viewerEntity.viewer.userKey
                notifications.add(notification)
                viewerEntity.displayYn = true
                viewerRepository.save(viewerEntity)
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
     * - MappingId??? ???????????? ?????????????????? ???????????? TokenDto??? ??????
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
     *  - SubProcess??? ????????? ????????? ????????? MappingId??? ?????? ????????? ????????? ???????????? ???????????? Dto ??????
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
     * ?????? ??????????????? ??????????????? [parentTokenId] ??? ??????????????? ?????? ??? ?????? ???????????? ????????? ?????????????????? ????????????
     */
    fun getCurrentAssigneeForChildProcess(parentTokenId: String): String? {
        val instance = wfInstanceRepository.findByPTokenId(parentTokenId) ?: return null
        val token = wfTokenRepository.findTopByInstanceOrderByTokenStartDtDesc(instance)
        return token?.assigneeId
    }

    /**
     * ?????? ?????????????????? CI ????????? ????????? ?????? ??????????????? ???????????? ??????
     * wf_component_ci_data ???????????? ???????????? ?????? ???????????? ????????? CI ???????????? ?????? ????????? ????????????.
     */
    fun copyComponentCIData(
        startTokenDto: WfTokenDto,
        makeDocumentTokenDto: WfTokenDto
    ) {
        val document = documentRepository.findDocumentEntityByDocumentId(makeDocumentTokenDto.documentId!!)
        val mainCIComponentList = mutableListOf<WfComponentEntity>()
        val subCIComponentList = mutableListOf<CICopyDataDto>()
        val subCIElementList = mutableListOf<WfElementEntity>()
        // ?????? ???????????? ??????????????? ???????????? ????????? ??????
        wfInstanceRepository.findByInstanceId(startTokenDto.instanceId)!!.document.form.components.forEach { component ->
            if (component.componentType == WfComponentConstants.ComponentTypeCode.CI.code && component.mappingId.isNotBlank()) {
                mainCIComponentList.add(component)
            }
        }

        // ?????? ??????????????? ?????????????????? ????????? ????????? ????????? CI??? ????????? ????????? ??????
        makeDocumentTokenDto.data?.let { wfTokenDataDtoList ->
            wfTokenDataDtoList.forEach { wfTokenData ->
                val component = wfComponentRepository.findByComponentId(wfTokenData.componentId)
                if (component.componentType == WfComponentConstants.ComponentTypeCode.CI.code && component.mappingId.isNotBlank()) {
                    if (wfTokenData.value.isNotBlank()) {
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
        }

        // ?????? ??????????????? ?????????????????? CI??? ????????? ???????????? ??????
        document.process.elementEntities.forEach { element ->
            element.elementDataEntities.forEach { elementData ->
                if (elementData.attributeId == WfElementConstants.AttributeId.SCRIPT_TYPE.value &&
                    elementData.attributeValue == WfElementConstants.ScriptType.DOCUMENT_CMDB.value
                ) {
                    subCIElementList.add(element)
                }
            }
        }

        // ?????? ??????????????? ?????????????????? ?????????????????? CI ???????????? ??? ??????????????? ???????????? ???????????? CI ???????????? ?????? ????????? ????????????.
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
     *  - ????????? ????????? ???????????? ??? mappingId ?????? ???????????? ????????? ????????? ??? Map?????? ?????? (key: componentId, value: mappingId)
     *  - ?????? ????????? ??? ?????? ???????????? ????????? ????????? ???????????? Map????????? ?????? (key: mappingId, value: tokenData value)
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

    /**
     *  Review ?????? ?????? ??????
     */
    fun updateReview(tokenDto: WfTokenDto): Boolean {
        val viewerKey = currentSessionUser.getUserKey()
        var isSuccess = false
        val instanceViewerEntity = viewerRepository.findByInstanceIdAndViewerKey(tokenDto.instanceId, viewerKey)
        if (instanceViewerEntity != null) {
            instanceViewerEntity.reviewYn = true
            viewerRepository.save(instanceViewerEntity)
            isSuccess = true
        }
        return isSuccess
    }
}
