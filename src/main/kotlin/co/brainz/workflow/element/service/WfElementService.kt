package co.brainz.workflow.element.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.token.repository.WfTokenDataRepository
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
    private val wfComponentRepository: WfComponentRepository,
    private val wfTokenDataRepository: WfTokenDataRepository
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
     * 다음 엘리먼트 정보 가져오기
     *
     * @param restTemplateTokenDto 종료된 엘리먼트의 토큰
     */
    fun getNextElement(wfTokenDto: WfTokenDto): WfElementEntity {
        val connector = this.getConnector(wfTokenDto)

        // 컨넥터 엘리먼트가 가지고 있는 타겟 엘리먼트 아이디 조회
        val nextElementId = wfElementDataRepository.findByElementAndAttributeId(connector).attributeValue
        return wfElementRepository.getOne(nextElementId)
    }

    /**
     * 컨넥터 엘리먼트를 조회.
     * 종료된 엘리먼트의 다음 엘리먼트 지정을 위한 컨넥터를 찾아서 리턴한다.
     *
     * @param restTemplateTokenDto 종료된 엘리먼트의 토큰
     */
    private fun getConnector(wfTokenDto: WfTokenDto): WfElementEntity {
        val elementId = wfTokenDto.elementId
        var selectedConnector: WfElementEntity? = null

        // 컨넥터를 가져와서
        val connectorElements = wfElementRepository.findAllArrowConnectorElement(elementId)

        // 컨넥터가 하나를 초과하면
        if (connectorElements.size > 1) {
            run main@{
                connectorElements.forEach connector@{ connectorElement ->

                    // 컨넥터의 source element id 를 이용하여 gw 엘리먼트를 조회 후 condition-item을 가져온다.
                    val gateWayElementId = wfElementDataRepository.findByElementAndAttributeId(
                        connectorElement,
                        WfElementConstants.AttributeId.SOURCE_ID.value
                    ).attributeValue
                    val conditionItem = wfElementDataRepository.findByElementAndAttributeId(
                        wfElementRepository.getOne(gateWayElementId),
                        WfElementConstants.AttributeId.CONDITION_ITEM.value
                    ).attributeValue.trim()
                    val item = this.getMatchesRegex(conditionItem, wfTokenDto)

                    // 컨넥터의 condition-value를 가져와서 symbol, value 로 분리한다.
                    val conditionValue = wfElementDataRepository.findByElementAndAttributeId(
                        connectorElement,
                        WfElementConstants.AttributeId.CONDITION_VALUE.value
                    ).attributeValue
                    val conditionValues = conditionValue.split("\\s+".toRegex())
                    val symbol = conditionValues[0]
                    val value = this.getMatchesRegex(conditionValues[1], wfTokenDto)

                    // symbol이 뭐냐에 따라 condition-item 과 condition-value가 일치하는 컨넥터 1개를 찾는다.
                    when (symbol) {
                        "=" -> {
                            if (item == value) {
                                selectedConnector = connectorElement
                                return@main
                            }
                        }
                        "<=" -> {
                            val val1 = item.toInt()
                            val val2 = value.toInt()
                            if (val1 <= val2) {
                                selectedConnector = connectorElement
                                return@main
                            }
                        }
                        "<" -> {
                            val val1 = item.toInt()
                            val val2 = value.toInt()
                            if (val1 < val2) {
                                selectedConnector = connectorElement
                                return@main
                            }
                        }
                        ">=" -> {
                            val val1 = item.toInt()
                            val val2 = value.toInt()
                            if (val1 >= val2) {
                                selectedConnector = connectorElement
                                return@main
                            }
                        }
                        ">" -> {
                            val val1 = item.toInt()
                            val val2 = value.toInt()
                            if (val1 > val2) {
                                selectedConnector = connectorElement
                                return@main
                            }
                        }
                    }
                }
            }
        } else { // 그 외 connector 가 1개일 때
            selectedConnector = connectorElements[0]
        }

        if (selectedConnector == null) {
            throw AliceException(
                AliceErrorConstants.ERR,
                "Not found arrowElement. check the element data. ex) condition"
            )
        }

        return selectedConnector as WfElementEntity
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
                        value = tokenData.value
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
