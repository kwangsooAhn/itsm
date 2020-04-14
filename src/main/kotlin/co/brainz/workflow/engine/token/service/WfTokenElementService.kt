package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.instance.dto.WfInstanceDto
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.engine.token.service.updatetoken.WfUpdateCommonToken
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfTokenElementService(
    private val wfTokenActionService: WfTokenActionService,
    private val wfActionService: WfActionService,
    private val wfTokenRepository: WfTokenRepository,
    private val wfInstanceService: WfInstanceService,
    private val wfElementService: WfElementService,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfDocumentRepository: WfDocumentRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Init Token.
     *
     * @param wfTokenDto
     */
    fun initToken(wfTokenDto: WfTokenDto) {
        val documentDto = wfTokenDto.documentId?.let { wfDocumentRepository.findDocumentEntityByDocumentId(it) }
        val instanceDto = documentDto?.let { WfInstanceDto(instanceId = "", document = it) }
        val instance = instanceDto?.let { wfInstanceService.createInstance(it) }

        val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(wfTokenDto.documentId!!)
        val startElement = wfElementService.getStartElement(wfDocumentEntity.process.processId)
        wfTokenDto.elementType = startElement.elementType
        wfTokenDto.elementId = startElement.elementId
        when (startElement.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                wfTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
            }
        }
        val startToken = wfTokenActionService.createToken(instance!!, wfTokenDto)
        wfTokenDto.tokenId = startToken.tokenId
        setTokenAction(wfTokenDto)
    }

    /**
     * Token Gate (Action).
     *
     * @param wfTokenDto
     */
    fun setTokenAction(wfTokenDto: WfTokenDto) {
        WfUpdateCommonToken(
            wfTokenActionService,
            wfActionService,
            wfTokenRepository,
            wfInstanceService,
            wfElementService,
            wfTokenDataRepository,
            wfDocumentRepository
        ).updateTokenAction(wfTokenDto)

    }
}
