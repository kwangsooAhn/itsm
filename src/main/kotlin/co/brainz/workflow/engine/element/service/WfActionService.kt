package co.brainz.workflow.engine.element.service

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementDataRepository
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.token.dto.WfActionDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfActionService(private val wfElementService: WfElementService,
                      private val wfElementRepository: WfElementRepository,
                      private val wfElementDataRepository: WfElementDataRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Document Start Init Actions.
     *
     * @param processId
     * @return MutableList<WfActionDto>
     */
    fun actionInit(processId: String): MutableList<WfActionDto> {
        val startElement = wfElementService.getStartElement(processId)
        val startArrow = getArrowElements(startElement.elementId)[0]
        val registerElementId = getNextElementId(startArrow)
        return actions(registerElementId)
    }

    fun actions(elementId: String): MutableList<WfActionDto> {
        val currentElement = getElement(elementId)
        val arrow = getArrowElements(elementId)[0]
        val nextElementId = getNextElementId(arrow)
        val nextElement = getElement(nextElementId)

        val actions: MutableList<WfActionDto> = mutableListOf()
        actions.addAll(preActions())
        actions.addAll(typeActions(arrow, nextElement))
        actions.addAll(postActions(currentElement))

        return actions
    }

    /**
     * Arrow Elements(Gateway Multiple).
     *
     * @param elementId
     * @return MutableList<WfElementEntity>
     */
    fun getArrowElements(elementId: String): MutableList<WfElementEntity> {
        return wfElementRepository.findAllArrowConnectorElement(elementId)
    }

    /**
     * Get Next Element Id.
     *
     * @param arrowElement
     * @return String
     */
    fun getNextElementId(arrowElement: WfElementEntity): String {
        return wfElementDataRepository.findByElementAndAttributeId(arrowElement).attributeValue
    }

    /**
     * Get Element.
     *
     * @param elementId
     * @return WfElementEntity
     */
    fun getElement(elementId: String): WfElementEntity {
        return wfElementRepository.findWfElementEntityByElementId(elementId)
    }

    /**
     * Pre Actions.
     *
     * @return MutableList<WfActionDto>
     */
    private fun preActions(): MutableList<WfActionDto> {
        val preActions: MutableList<WfActionDto> = mutableListOf()
        //SAVE
        preActions.add(WfActionDto(name = "저장", value = WfElementConstants.Action.SAVE.value))
        return preActions
    }

    /**
     * Post Actions.
     *
     * @param element
     * @return MutableList<WfActionDto>
     */
    private fun postActions(element: WfElementEntity): MutableList<WfActionDto> {
        val postActions: MutableList<WfActionDto> = mutableListOf()
        //REJECT: 현재 element 속성에 반려가 존재할 경우
        element.elementDataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.REJECT_ID.value && it.attributeValue.isNotEmpty()) {
                postActions.add(WfActionDto(name = "반려", value = WfElementConstants.Action.REJECT.value))
            }
        }

        return postActions
    }

    /**
     * ElementType Actions.
     *
     * @param arrow
     * @param nextElement
     * @return MutableList<WfActionDto>
     */
    private fun typeActions(arrow: WfElementEntity, nextElement: WfElementEntity): MutableList<WfActionDto> {
        val typeActions: MutableList<WfActionDto> = mutableListOf()
        when (nextElement.elementType) {
            WfElementConstants.ElementType.USER_TASK.value -> {
                typeActions.addAll(makeAction(arrow.elementDataEntities))
            }
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                typeActions.addAll(makeAction(arrow.elementDataEntities))
            }
            WfElementConstants.ElementType.SIGNAL_SEND.value -> {
                typeActions.addAll(makeAction(arrow.elementDataEntities))
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                var isAction = false
                nextElement.elementDataEntities.forEach { data ->
                    if (data.attributeId == WfElementConstants.AttributeId.CONDITION_ITEM.value) {
                        if (data.attributeValue == WfElementConstants.AttributeValue.ACTION.value) {
                            isAction = true
                        }
                    }
                }
                when (isAction) {
                    true -> {
                        val gatewayArrows = getArrowElements(nextElement.elementId)
                        gatewayArrows.forEach { gatewayArrow ->
                            typeActions.addAll(makeAction(gatewayArrow.elementDataEntities))
                        }
                    }
                    false -> {
                        typeActions.addAll(makeAction(arrow.elementDataEntities))
                    }
                }
            }
        }
        if (typeActions.isEmpty()) {
            typeActions.add(WfActionDto(name = "처리", value = WfElementConstants.Action.PROCESS.value))
        }

        return typeActions
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
            actionList.add(WfActionDto(name = actionName, value = actionValue))
        }
        return actionList
    }

}
