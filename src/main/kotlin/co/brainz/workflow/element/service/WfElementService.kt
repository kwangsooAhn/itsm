package co.brainz.workflow.element.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfElementService(
    private val wfElementRepository: WfElementRepository,
    private val wfElementDataRepository: WfElementDataRepository,
    private val wfComponentRepository: WfComponentRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * get element by ProcessId, ElementType
     *
     * @param processId
     * @return ElementEntity
     */
    fun getStartElement(processId: String): WfElementEntity {
        // TODO
        // start element는 몇 종류가 있는데 1개의 프로세스에 commonStart는 1개만 존재하지만 다른 종류의 start는 복수개가 존재가능.
        // 이에 대한 처리를 위해 getStartElement에 파라미터가 추가될 필요가 있다.
        return wfElementRepository.findByProcessIdAndElementType(
            processId,
            WfElementConstants.ElementType.COMMON_START_EVENT.value
        )
    }

    /**
     * Get end element.
     */
    fun getEndElement(processId: String): WfElementEntity {
        return wfElementRepository.findByProcessIdAndElementType(
            processId,
            WfElementConstants.ElementType.COMMON_END_EVENT.value
        )
    }

    /**
     * 다음 엘리먼트 정보 가져오기
     *
     * @param wfTokenDto 종료된 엘리먼트의 토큰
     */
    fun getNextElement(wfTokenDto: WfTokenDto): WfElementEntity {
        val connector = this.getConnector(wfTokenDto)

        // 컨넥터 엘리먼트가 가지고 있는 타겟 엘리먼트 아이디 조회
        val nextElementId = wfElementDataRepository.findByElementAndAttributeId(connector).attributeValue
        return wfElementRepository.getOne(nextElementId)
    }

    /**
     * 현재 element[tokenDto] 정보로 연결된 connector 수에 따라 분기.
     * 다수의 connector 가 존재할 경우 설정 옵션에 따라 한 개를 선택.
     */
    private fun getConnector(tokenDto: WfTokenDto): WfElementEntity {
        var selectedConnector: WfElementEntity? = null
        val connectorElements = wfElementRepository.findAllArrowConnectorElement(tokenDto.elementId)
        if (connectorElements.isNotEmpty()) {
            selectedConnector = when (connectorElements.size) {
                1 -> connectorElements[0]
                else -> this.getConnectorElement(tokenDto, connectorElements)
            }
        }

        if (selectedConnector == null) {
            throw AliceException(
                AliceErrorConstants.ERR,
                "Not found arrowElement. check the element data. ex) condition"
            )
        }

        return selectedConnector
    }

    /**
     * 다수의 연결선 중에 하나 선택.
     */
    private fun getConnectorElement(
        tokenDto: WfTokenDto,
        connectorElements: MutableList<WfElementEntity>
    ): WfElementEntity? {
        val connectorElement: WfElementEntity?
        // 현재 element에 condition-item 값을 찾는다.
        val conditionItem = wfElementDataRepository.findByElementAndAttributeId(
            wfElementRepository.getOne(tokenDto.elementId),
            WfElementConstants.AttributeId.CONDITION_ITEM.value
        ).attributeValue.trim()

        // Action: tokenDto.action
        // Condition: ${} 제거(MappingId) -> 연결된 component 조회 -> component와 연결된 token_data 에서 실제 데이터 조회
        val conditionItemValue = this.getMatchesRegex(conditionItem, tokenDto)

        connectorElement = when (conditionItem) {
            WfElementConstants.AttributeValue.ACTION.value -> this.getConnectorElementByAction(
                connectorElements,
                conditionItemValue,
                tokenDto
            )
            else -> this.getConnectorElementByCondition(connectorElements, conditionItemValue, tokenDto)
        }

        return connectorElement
    }

    /**
     * Connector 정보 가져오기 - Action일 경우.
     */
    private fun getConnectorElementByAction(
        connectorElements: MutableList<WfElementEntity>,
        conditionItemValue: String,
        tokenDto: WfTokenDto
    ): WfElementEntity? {
        var selectedConnector: WfElementEntity? = null
        connectorElements.forEach { connector ->
            val connectorValue = wfElementDataRepository.findByElementAndAttributeId(
                connector,
                WfElementConstants.AttributeId.ACTION_VALUE.value
            ).attributeValue
            if (conditionItemValue == this.getMatchesRegex(connectorValue, tokenDto)) {
                selectedConnector = connector
                return@forEach
            }
        }

        return selectedConnector
    }

    /**
     * Connector 정보 가져오기 - Condition일 경우.
     */
    private fun getConnectorElementByCondition(
        connectorElements: MutableList<WfElementEntity>,
        conditionItemValue: String,
        tokenDto: WfTokenDto
    ): WfElementEntity? {
        var selectedConnector: WfElementEntity? = null
        connectorElements.forEach { connector ->
            val connectorValue = wfElementDataRepository.findByElementAndAttributeId(
                connector,
                WfElementConstants.AttributeId.CONDITION_VALUE.value
            ).attributeValue
            val connectorValueSplitArray =
                connectorValue.replace("\\s+".toRegex(), "").split("(?=[a-zA-Z0-9])".toRegex(), 2)
            if (connectorValueSplitArray.size == 2) {
                val connectorConvertValue = this.getMatchesRegex(connectorValueSplitArray[1], tokenDto)
                when (connectorValueSplitArray[0]) {
                    "=" -> {
                        if (conditionItemValue == connectorConvertValue) {
                            selectedConnector = connector
                            return@forEach
                        }
                    }
                    "<=" -> {
                        if (conditionItemValue.toInt() <= connectorConvertValue.toInt()) {
                            selectedConnector = connector
                            return@forEach
                        }
                    }
                    "<" -> {
                        if (conditionItemValue.toInt() < connectorConvertValue.toInt()) {
                            selectedConnector = connector
                            return@forEach
                        }
                    }
                    ">=" -> {
                        if (conditionItemValue.toInt() >= connectorConvertValue.toInt()) {
                            selectedConnector = connector
                            return@forEach
                        }
                    }
                    ">" -> {
                        if (conditionItemValue.toInt() > connectorConvertValue.toInt()) {
                            selectedConnector = connector
                            return@forEach
                        }
                    }
                }
            }

            if (selectedConnector == null) {
                val isDefault = wfElementDataRepository.findByElementAndAttributeId(
                    connector,
                    WfElementConstants.AttributeId.IS_DEFAULT.value
                ).attributeValue == "Y"
                if (isDefault) {
                    selectedConnector = connector
                }
            }
        }

        return selectedConnector
    }

    /**
     * 프로세스 디자이너에서 condition에 사용하는 문법 구조에 해당하는 실제 데이터를 가져온다.
     * 일반, 엘리먼트 mappingid(${value}), 클라이언트에서 넘어오는 버튼(#{action}) 등
     */
    private fun getMatchesRegex(stringForRegex: String, wfTokenDto: WfTokenDto): String {
        val regexGeneral = WfElementConstants.RegexCondition.GENERAL.value.toRegex()
        val regexComponentMappingId = WfElementConstants.RegexCondition.MAPPINGID.value.toRegex()
        val regexConstant = WfElementConstants.RegexCondition.CONSTANT.value.toRegex()
        return when {
            stringForRegex.matches(regexComponentMappingId) -> {
                val mappingId = stringForRegex.trim().replace("\${", "").replace("}", "")
                // mappingId 가 설정된 componentId를 찾는다.
                var value = ""
                val componentIds: MutableList<String> = mutableListOf()
                wfTokenDto.data?.forEach { tokenData ->
                    componentIds.add(tokenData.componentId)
                }
                val wfComponentEntity =
                    wfComponentRepository.findByComponentIdInAndMappingId(
                        componentIds,
                        mappingId
                    )
                val componentId = wfComponentEntity.componentId
                wfTokenDto.data?.forEach { tokenData ->
                    if (tokenData.componentId == componentId) {
                        value = tokenData.value.split("|")[0]
                    }
                }
                return value
            }
            stringForRegex.matches(regexConstant) -> {
                var actionValue = ""
                if (stringForRegex == WfElementConstants.AttributeValue.ACTION.value) {
                    actionValue = wfTokenDto.action ?: ""
                }
                actionValue
            }
            stringForRegex.matches(regexGeneral) -> {
                stringForRegex.trim().replace("\"", "")
            }
            else -> stringForRegex.trim()
        }
    }
}
