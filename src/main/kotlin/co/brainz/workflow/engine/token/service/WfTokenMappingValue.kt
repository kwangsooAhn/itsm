package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import org.springframework.stereotype.Service

/**
 * 매핑 ID 정보로 토큰 데이터 관련 정보를 생성하는 클래스
 */
@Service
class WfTokenMappingValue(
    private val documentRepository: WfDocumentRepository
) {

    /**
     * 생성 할 업무의 mappingId 와 일치하는 토큰데이터를 찾아 dto 를 리턴.
     */
    fun makeRestTemplateTokenDto(token: WfTokenEntity, documentId: List<String>): List<RestTemplateTokenDto> {

        val keyPairComponentIdToMappingId = this.getTokenDataByMappingId(token)

        val tokensDto = mutableListOf<RestTemplateTokenDto>()
        documentId.forEach {
            val document = documentRepository.findDocumentEntityByDocumentId(it)

            val tokenDataList = mutableListOf<RestTemplateTokenDataDto>()
            document.form.components!!.forEach { component ->
                if (component.mappingId.isNotBlank() && keyPairComponentIdToMappingId[component.mappingId] != null) {
                    val value = keyPairComponentIdToMappingId[component.mappingId] as String
                    val data = RestTemplateTokenDataDto(componentId = component.componentId, value = value)
                    tokenDataList.add(data)
                }
            }

            tokensDto.add(
                RestTemplateTokenDto(
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
     * 토큰 정보를 조회하여 mappingId 와 매핑된 토큰데이터를 리턴.
     */
    private fun getTokenDataByMappingId(token: WfTokenEntity): MutableMap<String, String> {

        // 종료된 토큰의 componentId 별로 mappingId를 찾는다.
        val component = token.instance.document?.form?.components?.filter {
            it.mappingId.isNotBlank()
        }

        val keyPairComponentIdToMappingId = component?.associateBy({ it.componentId }, { it.mappingId })

        // mappingId 별로 실제 토큰에 저장된 value를 찾아 복제할 데이터를 생성한다.
        val keyPairMappingIdToTokenDataValue = mutableMapOf<String, String>()
        token.tokenDatas?.forEach {
            if (keyPairComponentIdToMappingId?.get(it.componentId) != null) {
                val mappingId = keyPairComponentIdToMappingId[it.componentId] as String
                keyPairMappingIdToTokenDataValue[mappingId] = it.value
            }
        }

        return keyPairMappingIdToTokenDataValue
    }
}
