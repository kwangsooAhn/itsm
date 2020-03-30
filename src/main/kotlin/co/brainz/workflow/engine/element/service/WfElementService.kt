package co.brainz.workflow.engine.element.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementDataRepository
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.token.dto.WfActionDto
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
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
     * @param wfTokenDto 종료된 엘리먼트의 토큰
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
     * @param wfTokenDto 종료된 엘리먼트의 토큰
     */
    private fun getConnector(wfTokenDto: WfTokenDto): WfElementEntity {
        val elementId = wfTokenDto.elementId
        lateinit var connectorElement: WfElementEntity
        val arrowConnectors = wfElementRepository.findAllArrowConnectorElement(elementId)

        // 게이트웨이로 인해 connector가 복수개일때 connector의 condition값으로 다음 element를 선택한다.
        if (arrowConnectors.size > 1) {
            run main@{
                arrowConnectors.forEach connector@{ arrowConnector ->
                    arrowConnector.elementDataEntities.forEach {
                        // 엘리먼트 세부 설정 속성값이 분기 조건 데이터일 때
                        if (it.attributeId == WfElementConstants.AttributeId.CONDITION.value) {
                            val regexGeneral = WfElementConstants.RegexCondition.GENERAL.value.toRegex()
                            val regexComponentMappingId = WfElementConstants.RegexCondition.MAPPINGID.value.toRegex()
                            val regexConstant = WfElementConstants.RegexCondition.CONSTANT.value.toRegex()

                            val getDoubleQuotationValue = regexGeneral.findAll(it.attributeValue)

                            val compareValues = mutableListOf<String>()

                            var symbol = it.attributeValue
                            getDoubleQuotationValue.forEach { doubleQuotation ->
                                // 부등호만 가져온다.
                                symbol = symbol.replace(doubleQuotation.value, "")

                                // 분기 조건 값, 따옴표로 둘러쌓여 있다.
                                val mappingIdForRegex = doubleQuotation.value

                                // 분기 조건 값에서 따옴표("), $, # 등 특수문자를 제거한 원본 값.
                                val mappingId =
                                    doubleQuotation.value.replace("\"", "").replace("\${", "").replace("}", "")

                                // 분기 조건 값에 따라 비교할 데이터를 조회한다. (${value}, #{value}, 일반)
                                val value = when {

                                    // ${value} 값
                                    mappingIdForRegex.matches(regexComponentMappingId) -> {
                                        val tokenId = wfTokenDto.tokenId
                                        val tokenDatas = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
                                        val componentIds = tokenDatas.map { tokenData ->
                                            tokenData.componentId
                                        }
                                        val wfComponentEntity =
                                            wfComponentRepository.findByComponentIdInAndMappingId(
                                                componentIds,
                                                mappingId
                                            )
                                        val componentId = wfComponentEntity.componentId

                                        val tokenData = wfTokenDataRepository.findByTokenIdAndComponentId(
                                            tokenId,
                                            componentId
                                        )
                                        tokenData.value
                                    }

                                    // #{value} 값
                                    mappingIdForRegex.matches(regexConstant) -> {
                                        wfTokenDto.assigneeId as String
                                    }

                                    // 일반 상수 값
                                    else -> mappingId
                                }

                                compareValues.add(value)
                            }

                            // 비교 대상이 2건을 벗어나거나 잘못 셋팅되었거나할 때 에러를 발생시킨다.
                            if (compareValues.size != 2) {
                                throw AliceException(
                                    AliceErrorConstants.ERR,
                                    "There are two values for comparing gateway branch conditions."
                                )
                            }

                            // TODO 2020-03-19 kbh - 부등호(symbol)에 따라 최종값 비교 기능을 추가 해야함. 현재는 동일 조건(== 만 동작한다.
                            symbol = symbol.trim()

                            // 최종 값을 비교
                            if (compareValues[0] == compareValues[1]) {
                                connectorElement = arrowConnector
                                return@main
                            } else {
                                return@connector
                            }
                        }
                    }
                }
            }

        } else { // 그 외 connector 가 1개일 때
            connectorElement = arrowConnectors[0]
        }

        return connectorElement
    }

    /**
     * Set Actions.
     *
     * @param wfTokenDto
     * @return MutableList<WfActionDto>
     */
    fun getActionList(wfTokenDto: WfTokenDto): MutableList<WfActionDto> {
        val connector = this.getConnector(wfTokenDto)
        val nextElement = getNextElement(wfTokenDto)

        val actionList: MutableList<WfActionDto> = mutableListOf()
        val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        when (nextElement.elementType) {
            WfElementConstants.ElementType.USER_TASK.value,
            WfElementConstants.ElementType.END_EVENT.value,
            WfElementConstants.ElementType.SIGNAL_EVENT.value -> {
                nextElement.elementDataEntities.forEach {
                    if (it.attributeId == WfElementConstants.AttributeId.ACTION.value) {
                        val buttonMap: LinkedHashMap<*, *>? = mapper.readValue(it.attributeValue, LinkedHashMap::class.java)
                        if (buttonMap != null) {
                            for ((key, value) in buttonMap) {
                                actionList.add(WfActionDto(name = key.toString(), value = value.toString()))
                            }
                        }
                    }
                }
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                //GW의 condition field가 #{action} 이라면 GW 에서 나가는 화살표의 속성을 이용해서 action 값 설정
                //#{action} 이 아니라면 GW로 들어가는 화살표의 속성을 이용해서 action 값 구성.
                nextElement.elementDataEntities.forEach {
                    if (it.attributeId == WfElementConstants.AttributeId.CONDITION.value) {
                        if (it.attributeValue.contains("#{action}")) {
                            //TODO: 다음 Task 로 연결되는 Arrow 의 정보를 찾아 추가
                            //Output Arrow Attribute
                            val nextConnector: WfElementEntity = getNextConnector(it.attributeValue)
                            nextConnector.elementDataEntities.forEach { data ->
                                if (data.attributeId == WfElementConstants.AttributeId.ACTION.value) {
                                    actionList.add(WfActionDto(name = "", value = ""))
                                }
                            }
                        } else {
                            //TODO: nextElement 앞의 Arrow 의 속성으로 추가
                            //Input Arrow Attribute
                            connector.elementDataEntities.forEach { data ->
                                if (data.attributeId == WfElementConstants.AttributeId.ACTION.value) {
                                    actionList.add(WfActionDto(name = "", value = ""))
                                }
                            }
                        }
                    }
                }
            }
            else -> actionList.add(WfActionDto(name = "progress", value = "처리"))
        }

        return actionList
    }

    /**
     * Get Next Connector.
     *
     * @param codition
     * @return WfElementEntity
     */
    fun getNextConnector(codition: String): WfElementEntity {
        return WfElementEntity(
                elementId = "",
                elementName = "",
                elementType = "",
                elementConfig = ""
        )
    }

}
