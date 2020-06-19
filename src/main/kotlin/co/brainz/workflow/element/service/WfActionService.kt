package co.brainz.workflow.element.service

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.provider.dto.RestTemplateActionDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfActionService(
    private val wfElementService: WfElementService,
    private val wfElementRepository: WfElementRepository,
    private val wfElementDataRepository: WfElementDataRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Document Start Init Actions.
     *
     * @param processId
     * @return MutableList<RestTemplateActionDto>
     */
    fun actionInit(processId: String): MutableList<RestTemplateActionDto> {
        val startElement = wfElementService.getStartElement(processId)
        val startArrow = getArrowElements(startElement.elementId)[0]
        val registerElementId = getNextElementId(startArrow)
        return actions(registerElementId)
    }

    fun actions(elementId: String): MutableList<RestTemplateActionDto> {
        val actions: MutableList<RestTemplateActionDto> = mutableListOf()
        val currentElement = getElement(elementId)
        if (currentElement.elementType != WfElementConstants.ElementType.COMMON_END_EVENT.value) {
            val arrow = getArrowElements(elementId)[0]
            val nextElementId = getNextElementId(arrow)
            val nextElement = getElement(nextElementId)

            actions.addAll(preActions())
            actions.addAll(typeActions(arrow, nextElement))
            actions.addAll(postActions(currentElement))
        }
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
     * @return MutableList<RestTemplateActionDto>
     */
    private fun preActions(): MutableList<RestTemplateActionDto> {
        val preActions: MutableList<RestTemplateActionDto> = mutableListOf()
        // SAVE
        preActions.add(RestTemplateActionDto(name = "저장", value = WfElementConstants.Action.SAVE.value))
        return preActions
    }

    /**
     * Post Actions.
     *
     * @param element
     * @return MutableList<RestTemplateActionDto>
     */
    private fun postActions(element: WfElementEntity): MutableList<RestTemplateActionDto> {
        val postActions: MutableList<RestTemplateActionDto> = mutableListOf()
        // REJECT: 현재 element 속성에 반려가 존재할 경우
        element.elementDataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.REJECT_ID.value && it.attributeValue.isNotEmpty()) {
                postActions.add(RestTemplateActionDto(name = "반려", value = WfElementConstants.Action.REJECT.value))
            }
        }

        return postActions
    }

    /**
     * ElementType Actions.
     *
     * @param arrow
     * @param nextElement
     * @return MutableList<RestTemplateActionDto>
     */
    private fun typeActions(arrow: WfElementEntity, nextElement: WfElementEntity): MutableList<RestTemplateActionDto> {
        val typeActions: MutableList<RestTemplateActionDto> = mutableListOf()
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
            typeActions.add(RestTemplateActionDto(name = "처리", value = WfElementConstants.Action.PROCESS.value))
        }

        return typeActions
    }

    /**
     * Make Actions.
     *
     * @param dataEntities
     * @return MutableList<RestTemplateActionDto>
     */
    private fun makeAction(dataEntities: MutableList<WfElementDataEntity>): MutableList<RestTemplateActionDto> {
        val actionList: MutableList<RestTemplateActionDto> = mutableListOf()
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
            actionList.add(RestTemplateActionDto(name = actionName, value = actionValue))
        }
        return actionList
    }
}
