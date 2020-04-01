package co.brainz.workflow.engine.element.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
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
import java.util.stream.Stream

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
     * @param wfTokenDto 종료된 엘리먼트의 토큰
     */
    fun getNextElement(wfTokenDto: WfTokenDto): WfElementEntity {
        val connector = this.getConnector(wfTokenDto)
        // 컨넥터 엘리먼트가 가지고 있는 타겟 엘리먼트 아이디 조회
        val nextElementId = wfElementDataRepository.findByElementAndAttributeId(connector).attributeValue
        return wfElementRepository.getOne(nextElementId)
    }


    fun arrowElement(elementId: String): WfElementEntity {
        lateinit var element: WfElementEntity
        val arrowElements = wfElementRepository.findAllArrowConnectorElement(elementId)
        if (arrowElements.isNotEmpty()) {
            element = when (arrowElements.size) {
                0 -> arrowElements[0]
                else -> {
                    getArrowElement(arrowElements)
                }
            }
        }
        return element
    }

    private fun getArrowElement(arrowElements: MutableList<WfElementEntity>): WfElementEntity {
        arrowElements.forEach { arrow ->
            arrow.elementDataEntities.forEach { data ->
                if (data.attributeId == WfElementConstants.AttributeId.CONDITION.value) {
                    val regexGeneral = WfElementConstants.RegexCondition.GENERAL.value.toRegex()
                    val regexComponentMappingId = WfElementConstants.RegexCondition.MAPPINGID.value.toRegex()
                    val regexConstant = WfElementConstants.RegexCondition.CONSTANT.value.toRegex()
                    val getDoubleQuotationValue = regexGeneral.findAll(data.attributeValue)
                    val compareValues = mutableListOf<String>()
                    var symbol = data.attributeValue
                    getDoubleQuotationValue.forEach { doubleQuotation ->
                        symbol = symbol.replace(doubleQuotation.value, "")
                        val mappingIdForRegex = doubleQuotation.value
                        val mappingId = doubleQuotation.value.replace("\"", "").replace("\${", "").replace("}", "")

                    }
                }
            }
        }
        return arrowElements[0]
    }

    //elementId로 현재 토큰과 다음 토큰, 연결괸 Arrow 정보를 가져와야 한다.
    //없으면.. 최초 시??
    fun action(elementId: String): MutableList<WfActionDto>  {
        val currentElement = getElement(elementId) //현재 정보
        val arrowElement = arrowElement(elementId) //다음 연결 화살표
        val nextElementId = wfElementDataRepository.findByElementAndAttributeId(arrowElement).attributeValue //다음 정보 아이디
        val nextElement = getElement(nextElementId)

        println(">>>>>>>>>>>>>>>")
        println(currentElement)
        println(arrowElement)
        println(nextElement)
        println(">>>>>>>>>>>>>>>")

        val actions: MutableList<WfActionDto> = mutableListOf()
        actions.addAll(preActions(nextElement))
        actions.addAll(typeActions(nextElement))
        actions.addAll(postActions(nextElement))
        return actions
    }

    private fun typeActions(element: WfElementEntity): MutableList<WfActionDto> {
        val typeActions: MutableList<WfActionDto> = mutableListOf()
        when (element.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                typeActions.add(WfActionDto(name = "등록", value = WfElementConstants.Action.REGIST.value))
            }
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {

            }
            WfElementConstants.ElementType.USER_TASK.value -> {
                typeActions.addAll(makeAction(element.elementDataEntities))
            }
            WfElementConstants.ElementType.END_EVENT.value -> {
                typeActions.addAll(makeAction(element.elementDataEntities))
            }
            WfElementConstants.ElementType.SIGNAL_EVENT.value -> {
                typeActions.addAll(makeAction(element.elementDataEntities))
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                val arrowElement = arrowElement(element.elementId)
                element.elementDataEntities.forEach {
                    if (it.attributeId == WfElementConstants.AttributeId.CONDITION.value) {
                        when (it.attributeValue.contains("#{action}")) {
                            true -> {

                            }
                            false -> {

                            }
                            /*true -> {
                                val arrowConnectors = wfElementRepository.findAllArrowConnectorElement(nextElement.elementId)
                                var nextConnector: WfElementEntity = arrowConnectors[0]
                                if (arrowConnectors.size > 1) {
                                    //TODO: 조건에 따른 연결 커넥션 선택 (아래는 예시)
                                    nextConnector = arrowConnectors[0]
                                }
                                actionList.addAll(makeAction(nextConnector.elementDataEntities))
                            }
                            false -> {
                                actionList.addAll(makeAction(connector.elementDataEntities))
                            }*/
                        }
                    }
                }
            }

        }

        if (typeActions.isEmpty()) {
            typeActions.add(WfActionDto(name = "처리", value = WfElementConstants.Action.PROCESS.value))
        }

        return typeActions
    }

    private fun preActions(element: WfElementEntity): MutableList<WfActionDto> {
        val preActions: MutableList<WfActionDto> = mutableListOf()

        //SAVE (Common)
        preActions.add(WfActionDto(name = "저장", value = WfElementConstants.Action.SAVE.value))

        //TODO: Add Preview Actions

        return preActions
    }

    private fun postActions(element: WfElementEntity): MutableList<WfActionDto> {
        val postActions: MutableList<WfActionDto> = mutableListOf()

        //REJECT
        element.elementDataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.REJECT.value && it.attributeValue.isNotEmpty()) {
                postActions.add(WfActionDto(name = "반려", value = WfElementConstants.Action.REJECT.value))
            }
        }

        //TODO: Add Post Actions

        return postActions
    }


    fun getElement(elementId: String): WfElementEntity {
        return wfElementRepository.findWfElementEntityByElementId(elementId)
    }

    /**
     * Set Actions.
     *
     * @param wfTokenDto
     * @return MutableList<WfActionDto>
     */
    fun getActionList(wfTokenDto: WfTokenDto): MutableList<WfActionDto> {
        val connector = this.getConnector(wfTokenDto)
        val currentElement = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        val nextElement = getNextElement(wfTokenDto)

        val actionList: MutableList<WfActionDto> = mutableListOf()

        //attributeId : save
        actionList.add(WfActionDto(name = "저장", value = WfElementConstants.Action.SAVE.value))

        when (nextElement.elementType) {
            WfElementConstants.ElementType.USER_TASK.value,
            WfElementConstants.ElementType.END_EVENT.value,
            WfElementConstants.ElementType.SIGNAL_EVENT.value -> {
                actionList.addAll(makeAction(connector.elementDataEntities))
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                nextElement.elementDataEntities.forEach {
                    if (it.attributeId == WfElementConstants.AttributeId.CONDITION.value) {
                        when (it.attributeValue.contains("#{action}")) {
                            true -> {
                                val arrowConnectors = wfElementRepository.findAllArrowConnectorElement(nextElement.elementId)
                                var nextConnector: WfElementEntity = arrowConnectors[0]
                                if (arrowConnectors.size > 1) {
                                    //TODO: 조건에 따른 연결 커넥션 선택 (아래는 예시)
                                    nextConnector = arrowConnectors[0]
                                }
                                actionList.addAll(makeAction(nextConnector.elementDataEntities))
                            }
                            false -> {
                                actionList.addAll(makeAction(connector.elementDataEntities))
                            }
                        }
                    }
                }
            }
            else -> actionList.add(WfActionDto(name = "처리", value = WfElementConstants.Action.PROCESS.value))
        }

        //attributeId : action reject
        currentElement.elementDataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.REJECT.value && it.attributeValue.isNotEmpty()) {
                actionList.add(WfActionDto(name = "반려", value = WfElementConstants.Action.REJECT.value))
            }
        }

        return actionList
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
     * Make Actions.
     *
     * @param dataEntities
     * @return MutableList<WfActionDto>
     */
    private fun makeAction(dataEntities: MutableList<WfElementDataEntity>): MutableList<WfActionDto> {
        val actionList: MutableList<WfActionDto> = mutableListOf()
        var actionName = ""
        var actionValue = ""
        dataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.ACTION_NAME.value) {
                actionName = it.attributeValue
            }
            if (it.attributeId == WfElementConstants.AttributeId.ACTION_VALUE.value) {
                actionValue = it.attributeValue
            }
        }
        if (actionName.isNotEmpty() && actionValue.isNotEmpty()) {
            actionList.add(WfActionDto(name = actionValue, value = actionName))
        }
        return actionList
    }

}
