package co.brainz.workflow.engine.manager.service

import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.service.NotificationService
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
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfCandidateEntity
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfCandidateRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import java.time.LocalDateTime
import java.time.ZoneId
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
    private val wfCandidateRepository: WfCandidateRepository,
    private val aliceUserRoleMapRepository: AliceUserRoleMapRepository
) {

    /**
     * Get element entity.
     *
     * @param elementId
     * @return WfElementEntity
     */
    fun getElement(elementId: String): WfElementEntity {
        return wfElementRepository.findWfElementEntityByElementId(elementId)
    }

    /**
     * Get component value(split[0]).
     *
     * @param tokenId
     * @param mappingId
     * @return String
     */
    fun getComponentValue(tokenId: String, mappingId: String): String {
        return wfTokenDataRepository.findByTokenIdAndComponentId(tokenId, mappingId).value.split("|")[0]
    }

    /**
     * Save all candidate.
     *
     * @param candidateEntities
     * @return List<WfCandidateEntity>
     */
    fun saveAllCandidate(candidateEntities: MutableList<WfCandidateEntity>): List<WfCandidateEntity> {
        return wfCandidateRepository.saveAll(candidateEntities)
    }

    /**
     * Create instance.
     *
     * @param wfTokenDto
     * @return WfInstanceEntity
     */
    fun createInstance(wfTokenDto: WfTokenDto): WfInstanceEntity {
        return wfInstanceService.createInstance(wfTokenDto)
    }

    /**
     * Complete instance.
     *
     * @param instanceId
     */
    fun completeInstance(instanceId: String) {
        return wfInstanceService.completeInstance(instanceId)
    }

    /**
     * Get start element.
     *
     * @param processId
     * @return WfElementEntity
     */
    fun getStartElement(processId: String): WfElementEntity {
        return wfElementService.getStartElement(processId)
    }

    /**
     * Get next element.
     *
     * @param wfTokenDto
     * @return WfElementEntity
     */
    fun getNextElement(wfTokenDto: WfTokenDto): WfElementEntity {
        return wfElementService.getNextElement(wfTokenDto)
    }

    /**
     * Get token.
     *
     * @param tokenId
     * @return WfTokenEntity
     */
    fun getToken(tokenId: String): WfTokenEntity {
        return wfTokenRepository.findTokenEntityByTokenId(tokenId).get()
    }

    /**
     * Save token.
     *
     * @param tokenEntity
     * @return WfTokenEntity
     */
    fun saveToken(tokenEntity: WfTokenEntity): WfTokenEntity {
        return wfTokenRepository.save(tokenEntity)
    }

    /**
     * Save all token data.
     *
     * @param tokenDataEntities
     * @return MutableList<WfTokenDataEntity>
     */
    fun saveAllTokenData(tokenDataEntities: MutableList<WfTokenDataEntity>): MutableList<WfTokenDataEntity> {
        return wfTokenDataRepository.saveAll(tokenDataEntities)
    }

    /**
     * Make token entity.
     *
     * @param wfTokenDto
     * @return WfTokenEntity
     */
    fun makeTokenEntity(wfTokenDto: WfTokenDto): WfTokenEntity {
        return WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(wfTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        )
    }

    /**
     * Save Notification.
     *
     * @param token
     * @param candidates
     */
    fun saveNotification(token: WfTokenEntity, candidates: List<WfCandidateEntity>? = null) {
        if (token.element.notification) {
            val notifications = mutableListOf<NotificationDto>()
            val commonNotification = NotificationDto(
                title = token.instance.document.documentName,
                message = "[${token.element.elementName}] ${token.instance.document.documentDesc}",
                instanceId = token.instance.instanceId
            )
            if (candidates != null) {
                candidates.forEach { candidate ->
                    when (candidate.candidateType) {
                        WfTokenConstants.AssigneeType.USERS.code -> {
                            val notification = commonNotification.copy()
                            notification.receivedUser = candidate.candidateValue
                            notifications.add(notification)
                        }
                        WfTokenConstants.AssigneeType.GROUPS.code -> {
                            val users = aliceUserRoleMapRepository.findUserRoleMapByRoleId(candidate.candidateValue)
                            users?.forEach {
                                val notification = commonNotification.copy()
                                notification.receivedUser = it.user.userKey
                                notifications.add(notification)
                            }
                        }
                    }
                }
            } else {
                commonNotification.receivedUser = token.assigneeId!!
                notifications.add(commonNotification)
            }
            notificationService.insertNotificationList(notifications)
        }
    }

    /**
     * Make mapping tokenDto.
     *
     * @param token
     * @param documentId
     * @return List<WfTokenDto>
     */
    fun makeMappingTokenDto(token: WfTokenEntity, documentId: List<String>): List<WfTokenDto> {
        val keyPairMappingIdAndTokenData = this.getTokenDataByMappingId(token)
        val tokensDto = mutableListOf<WfTokenDto>()
        documentId.forEach {
            val document = documentRepository.findDocumentEntityByDocumentId(it)
            val tokenDataList = mutableListOf<WfTokenDataDto>()
            document.form.components!!.forEach { component ->
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
     *
     * @param subProcessToken
     * @param mainProcessToken
     * @return List<WfTokenDataDto>
     */
    fun makeSubProcessTokenDataDto(
        subProcessToken: WfTokenEntity,
        mainProcessToken: WfTokenEntity
    ): List<WfTokenDataDto> {
        val keyPairMappingIdAndTokenData = this.getTokenDataByMappingId(subProcessToken)
        val componentIdAndTokenData = mutableMapOf<String, String>()
        mainProcessToken.instance.document.form.components!!.forEach {
            if (it.mappingId.isNotBlank() && keyPairMappingIdAndTokenData[it.mappingId] != null) {
                componentIdAndTokenData[it.componentId] = keyPairMappingIdAndTokenData[it.mappingId] as String
            }
        }
        val tokenData = mutableListOf<WfTokenDataDto>()
        mainProcessToken.tokenData?.forEach {
            val componentId = it.componentId
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
     * Get tokenData by mappingId.
     *
     * @param token
     * @return MutableMap<String, String>
     */
    private fun getTokenDataByMappingId(token: WfTokenEntity): MutableMap<String, String> {
        val component = token.instance.document.form.components?.filter {
            it.mappingId.isNotBlank()
        }
        val keyPairComponentIdToMappingId = component?.associateBy({ it.componentId }, { it.mappingId })
        val keyPairMappingIdToTokenDataValue = mutableMapOf<String, String>()
        token.tokenData?.forEach {
            val componentId = it.componentId
            if (keyPairComponentIdToMappingId?.get(componentId) != null) {
                val mappingId = keyPairComponentIdToMappingId[componentId] as String
                keyPairMappingIdToTokenDataValue[mappingId] = it.value
            }
        }

        return keyPairMappingIdToTokenDataValue
    }
}
