package co.brainz.workflow.engine

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.repository.WfTokenRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfEngine(
    private val wfInstanceService: WfInstanceService,
    private val wfElementService: WfElementService,
    private val wfTokenRepository: WfTokenRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfElementRepository: WfElementRepository
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
                wfTokenRepository
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

        println("PROCESS!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        logger.debug("Process Token")



        return true
    }
}
