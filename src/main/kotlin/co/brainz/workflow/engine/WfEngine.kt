package co.brainz.workflow.engine

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import co.brainz.workflow.token.service.WfTokenService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfEngine(
    private val wfInstanceService: WfInstanceService,
    private val wfElementService: WfElementService,
    private val wfTokenRepository: WfTokenRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfElementRepository: WfElementRepository,
    private val wfTokenDataRepository: WfTokenDataRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun startWorkflow(restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        logger.debug("Start Workflow")

        // Create Instance
        val instance = wfInstanceService.createInstance(restTemplateTokenDto)

        // StartToken Create & Complete
        val element = wfElementService.getStartElement(instance.document.process.processId)
        var startTokenDto = restTemplateTokenDto.copy()
        startTokenDto.instanceId = instance.instanceId
        startTokenDto.elementType = element.elementType
        startTokenDto.elementId = element.elementId
        val tokenManager =
            WfTokenManagerFactory(
                wfElementService,
                wfInstanceRepository,
                wfElementRepository,
                wfTokenRepository,
                wfTokenDataRepository
            ).getTokenManager(
                startTokenDto.elementType
            )
        startTokenDto = tokenManager.createToken(startTokenDto)
        startTokenDto = tokenManager.completeToken(startTokenDto)

        // FirstToken Create
        val firstTokenDto = tokenManager.createNextToken(startTokenDto)

        return processToken(firstTokenDto)
    }

    fun processToken(restTemplateTokenDto: RestTemplateTokenDto): Boolean {

        logger.debug("Process Token")

        when (restTemplateTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> {
                val token = wfTokenRepository.findTokenEntityByTokenId(restTemplateTokenDto.tokenId).get()
                token.assigneeId = restTemplateTokenDto.assigneeId
                wfTokenRepository.save(token)
                val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
                for (tokenDataDto in restTemplateTokenDto.data!!) {
                    val tokenDataEntity = WfTokenDataEntity(
                        tokenId = restTemplateTokenDto.tokenId,
                        componentId = tokenDataDto.componentId,
                        value = tokenDataDto.value
                    )
                    tokenDataEntities.add(tokenDataEntity)
                }
                if (tokenDataEntities.isNotEmpty()) {
                    wfTokenDataRepository.saveAll(tokenDataEntities)
                }
                wfTokenRepository.save(token)
            }
            else -> {
                do {
                    val token = wfTokenRepository.findTokenEntityByTokenId(restTemplateTokenDto.tokenId).get()
                    restTemplateTokenDto.instanceId = token.instance.instanceId
                    restTemplateTokenDto.elementType = token.element.elementType
                    restTemplateTokenDto.elementId = token.element.elementId

                    val tokenManager = WfTokenManagerFactory(
                        wfElementService,
                        wfInstanceRepository,
                        wfElementRepository,
                        wfTokenRepository,
                        wfTokenDataRepository
                    ).getTokenManager(restTemplateTokenDto.elementType)
                    tokenManager.completeToken(restTemplateTokenDto)
                    tokenManager.createNextToken(restTemplateTokenDto)
                } while (!restTemplateTokenDto.isComplete)
            }
        }



        return true
    }
}
