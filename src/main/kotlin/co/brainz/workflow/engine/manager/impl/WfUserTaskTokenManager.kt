package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfCandidateEntity
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfCandidateRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository

class WfUserTaskTokenManager(
    override val wfElementService: WfElementService,
    override val wfInstanceService: WfInstanceService,
    override val wfInstanceRepository: WfInstanceRepository,
    override val wfElementRepository: WfElementRepository,
    override val wfTokenRepository: WfTokenRepository,
    override val wfTokenDataRepository: WfTokenDataRepository,
    override val wfCandidateRepository: WfCandidateRepository
) : WfTokenManager() {

    lateinit var assigneeId: String

    override fun createToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        super.createToken(restTemplateTokenDto)
        super.wfTokenEntity.tokenData = wfTokenDataRepository.saveAll(setTokenData(restTemplateTokenDto))
        this.assigneeId = restTemplateTokenDto.assigneeId.toString()
        setCandidate(super.wfTokenEntity)

        return restTemplateTokenDto
    }

    override fun completeToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        super.completeToken(restTemplateTokenDto)
        super.wfTokenEntity.tokenData = wfTokenDataRepository.saveAll(setTokenData(restTemplateTokenDto))
        super.wfTokenEntity.assigneeId = restTemplateTokenDto.assigneeId
        wfTokenRepository.save(super.wfTokenEntity)

        return restTemplateTokenDto
    }

    /**
     * Set Assignee + Candidate.
     *
     */
    private fun setCandidate(token: WfTokenEntity) {
        val assigneeType =
            getAttributeValue(token.element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE_TYPE.value)
        when (assigneeType) {
            WfTokenConstants.AssigneeType.ASSIGNEE.code -> {
                var assigneeId = getAssignee(token.element, token)
                if (assigneeId.isEmpty()) {
                    assigneeId = this.assigneeId
                }
                token.assigneeId = assigneeId
                wfTokenRepository.save(token)
            }
            WfTokenConstants.AssigneeType.USERS.code,
            WfTokenConstants.AssigneeType.GROUPS.code -> {
                val candidates =
                    getAttributeValues(token.element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
                if (candidates.isNotEmpty()) {
                    val wfCandidateEntities = mutableListOf<WfCandidateEntity>()
                    candidates.forEach { candidate ->
                        val wfCandidateEntity = WfCandidateEntity(
                            token = token,
                            candidateType = assigneeType,
                            candidateValue = candidate
                        )
                        wfCandidateEntities.add(wfCandidateEntity)
                    }
                    wfCandidateRepository.saveAll(wfCandidateEntities)
                } else {
                    token.assigneeId = this.assigneeId
                    wfTokenRepository.save(token)
                }
            }
        }
    }

    /**
     * Get Assignee.
     *
     * @param element
     * @param token
     * @return String
     */
    private fun getAssignee(element: WfElementEntity, token: WfTokenEntity): String {
        val assigneeMappingId =
            getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
        var componentMappingId = ""
        token.instance.document.form.components?.forEach { component ->
            if (component.mappingId.isNotEmpty() && component.mappingId == assigneeMappingId) {
                componentMappingId = component.componentId
            }
        }
        var assignee = ""
        if (componentMappingId.isNotEmpty()) {
            assignee =
                wfTokenDataRepository.findByTokenIdAndComponentId(token.tokenId, componentMappingId).value.split("|")[0]
        }
        return assignee
    }

    /**
     * Set Token Data.
     *
     * @param restTemplateTokenDto
     * @return MutableList<WfTokenDataEntity>
     */
    private fun setTokenData(restTemplateTokenDto: RestTemplateTokenDto): MutableList<WfTokenDataEntity> {
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        for (tokenDataDto in restTemplateTokenDto.data!!) {
            val tokenDataEntity = WfTokenDataEntity(
                tokenId = restTemplateTokenDto.tokenId,
                componentId = tokenDataDto.componentId,
                value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        return tokenDataEntities
    }

    /**
     * Get AttributeValue.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return String (attributeValue)
     */
    private fun getAttributeValue(
        elementDataEntities: MutableList<WfElementDataEntity>,
        attributeId: String
    ): String {
        var attributeValue = ""
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValue = data.attributeValue
            }
        }
        return attributeValue
    }

    /**
     * Get AttributeValues.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return MutableList<String> (attributeValue)
     */
    private fun getAttributeValues(
        elementDataEntities: MutableList<WfElementDataEntity>,
        attributeId: String
    ): MutableList<String> {
        val attributeValues: MutableList<String> = mutableListOf()
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValues.add(data.attributeValue)
            }
        }
        return attributeValues
    }

}
