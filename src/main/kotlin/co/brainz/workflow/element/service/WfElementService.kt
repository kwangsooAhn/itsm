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
     * @param restTemplateTokenDto 종료된 엘리먼트의 토큰
     */
    fun getNextElement(wfTokenDto: WfTokenDto): WfElementEntity {
        val connector = this.getConnector(wfTokenDto)

        // 컨넥터 엘리먼트가 가지고 있는 타겟 엘리먼트 아이디 조회
        val nextElementId = wfElementDataRepository.findByElementAndAttributeId(connector).attributeValue
        return wfElementRepository.getOne(nextElementId)
    }

    /**
     * 파라미터로 전달 받은 토큰에 연결된 컨넥터를 조회해서 진행방향을 결정하고 결정된 컨넥터 엘리먼트를 반환.
     *
     * @param tokenDto 진행방향을 결정하려는 토큰
     */
    private fun getConnector(tokenDto: WfTokenDto): WfElementEntity {
        val currentElementId = tokenDto.elementId
        var selectedConnector: WfElementEntity? = null
        val connectorElements = wfElementRepository.findAllArrowConnectorElement(currentElementId)

        if (connectorElements.size > 1) { // 분기되는 게이트웨이
            run main@{
                val conditionItem = wfElementDataRepository.findByElementAndAttributeId(
                    wfElementRepository.getOne(currentElementId),
                    WfElementConstants.AttributeId.CONDITION_ITEM.value
                ).attributeValue.trim()

                val userValue = this.getMatchesRegex(conditionItem, tokenDto)

                /* Temporary Code
                 * 2020-06-20 Jung Hee Chan
                 * action을 이용할지 component value를 이용할지에 따라 처리가 달라지는데 이건 별도의 Refactoring이 필요하다.
                 * 기존에는 action-value인 경우에 대한 처리가 없었고 condition-value인 조건만 구현되어 있었음.
                 * 여기서는 일감 결함처리를 위해서 action 처리를 추가하기 위해서 사용함.
                 */
                val attributeId: String
                attributeId = if (conditionItem == ("#{action}")) {
                    WfElementConstants.AttributeId.ACTION_VALUE.value
                } else {
                    WfElementConstants.AttributeId.CONDITION_VALUE.value
                }

                connectorElements.forEach connector@{ connectorElement ->
                    val connectorCondition = wfElementDataRepository.findByElementAndAttributeId(
                        connectorElement,
                        attributeId
                    ).attributeValue

                    /* Temporary Code
                     * 2020-06-20 Jung Hee Chan
                     * 이것도 action, component value의 사용여부에 따라 수식이 다르기 때문에 위의 내용과 함께 별도의 Refactoring이 필요하다.
                     */
                    val conditionOperator: String
                    val conditionValue: String
                    if (attributeId == WfElementConstants.AttributeId.ACTION_VALUE.value) {
                        conditionOperator = "="
                        conditionValue = this.getMatchesRegex(connectorCondition, tokenDto)
                    } else {
                        val parsedConnectorCondition =
                            connectorCondition.replace("\\s+".toRegex(), "").split("(?=[a-zA-Z0-9])".toRegex(), 2)
                        conditionOperator = parsedConnectorCondition[0]
                        conditionValue = this.getMatchesRegex(parsedConnectorCondition[1], tokenDto)
                    }

                    when (conditionOperator) {
                        "=" -> {
                            if (userValue == conditionValue) {
                                selectedConnector = connectorElement
                                return@main
                            }
                        }
                        "<=" -> {
                            val val1 = userValue.toInt()
                            val val2 = conditionValue.toInt()
                            if (val1 <= val2) {
                                selectedConnector = connectorElement
                                return@main
                            }
                        }
                        "<" -> {
                            val val1 = userValue.toInt()
                            val val2 = conditionValue.toInt()
                            if (val1 < val2) {
                                selectedConnector = connectorElement
                                return@main
                            }
                        }
                        ">=" -> {
                            val val1 = userValue.toInt()
                            val val2 = conditionValue.toInt()
                            if (val1 >= val2) {
                                selectedConnector = connectorElement
                                return@main
                            }
                        }
                        ">" -> {
                            val val1 = userValue.toInt()
                            val val2 = conditionValue.toInt()
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
