package co.brainz.workflow.engine.token.service.updatetoken

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.folder.service.WfFolderService
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.token.dto.WfTokenDataDto
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.engine.token.service.WfTokenActionService
import co.brainz.workflow.engine.token.service.WfTokenService
import org.springframework.stereotype.Service

/**
 * 시그널 이벤트 토큰 업데이트
 */
@Service
class WfUpdateSignalToken(
    wfTokenActionService: WfTokenActionService,
    wfActionService: WfActionService,
    wfTokenRepository: WfTokenRepository,
    wfInstanceService: WfInstanceService,
    wfElementService: WfElementService,
    wfTokenDataRepository: WfTokenDataRepository,
    wfDocumentRepository: WfDocumentRepository,
    private val wfTokenService: WfTokenService,
    wfFolderService: WfFolderService

) : WfUpdateToken(
    wfTokenActionService,
    wfActionService,
    wfTokenRepository,
    wfInstanceService,
    wfElementService,
    wfTokenDataRepository,
    wfDocumentRepository,
    wfFolderService
) {
    override fun updateToken(wfTokenEntity: WfTokenEntity, wfElementEntity: WfElementEntity, wfTokenDto: WfTokenDto) {

        val tokenId = wfTokenDto.tokenId
        val targetDocuments = WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value

        // 현재 토큰 종료
        wfTokenActionService.setProcess(wfTokenEntity, wfTokenDto)

        // 종료된 토큰의 WfTokenDto.elementId 로 WfElementDataEntity 조회 후 target-document-list 확인
        val targetDocumentId = mutableListOf<String>()
        wfElementEntity.elementDataEntities.forEach {
            if (it.attributeId == targetDocuments) {
                targetDocumentId.add(it.attributeValue)
            }
        }

        // 종료된 토큰의 데이터(tokenData)를 확인하여 컴포넌트ID를 모두 찾는다.
        val tokenData = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
        val componentIdInTokenData = tokenData.map { it.componentId }

        // 종료된 토큰의 componentId 별로 mappingId를 찾는다.
        val token = wfTokenRepository.getOne(tokenId)
        val component = token.instance.document.form.components!!.filter {
            componentIdInTokenData.contains(it.componentId) && it.mappingId.isNotBlank()
        }
        val mappingIds = component.associateBy({ it.componentId }, { it.mappingId })

        // mappingId 별로 실제 토큰에 저장된 value를 찾아 복제할 데이터를 생성한다.
        val tokenDataForCopy = mutableMapOf<String, String>()
        tokenData.forEach {
            val mappingId = mappingIds[it.componentId] as String
            tokenDataForCopy[mappingId] = it.value
        }

        // target-document-list 별로 토큰을 초기화한다.
        targetDocumentId.forEach { documentId ->
            val document = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)

            // 복제할 데이터에서 타켓 다큐먼트의 mappingId 와 일치하는 value를 찾고
            // WfTokenDataDto 를 생성한다.
            val tokenDatas = mutableListOf<WfTokenDataDto>()
            document.form.components!!.forEach { component ->
                if (component.mappingId.isNotBlank()) {
                    val value = tokenDataForCopy[component.mappingId] as String
                    val data = WfTokenDataDto(componentId = component.componentId, value = value)
                    tokenDatas.add(data)
                }
            }

            // WfTokenDto 생성 후 토큰 실행!!
            wfTokenService.initToken(WfTokenDto(documentId = document.documentId, data = tokenDatas))
        }
    }
}
