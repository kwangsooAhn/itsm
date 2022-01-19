/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.element.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.instanceViewer.repository.WfInstanceViewerRepository
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
    private val wfElementDataRepository: WfElementDataRepository,
    private val wfInstanceViewerRepository: WfInstanceViewerRepository,
    private val currentSessionUser: CurrentSessionUser
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
        val startArrow = this.getArrowElements(startElement.elementId)[0]
        val registerElementId = this.getNextElementId(startArrow)
        return this.actions(registerElementId, null)
    }

    fun actions(elementId: String, instanceId: String?): MutableList<RestTemplateActionDto> {
        val actions: MutableList<RestTemplateActionDto> = mutableListOf()
        val currentElement = this.getElement(elementId)
        if (currentElement.elementType != WfElementConstants.ElementType.COMMON_END_EVENT.value) {
            val arrow = this.getArrowElements(elementId)[0]
            val nextElementId = this.getNextElementId(arrow)
            val nextElement = this.getElement(nextElementId)
            actions.addAll(this.preActions(instanceId))
            actions.addAll(this.typeActions(arrow, nextElement))
            actions.addAll(this.postActions(currentElement))
        }
        return actions
    }

    /**
     * Arrow Elements(Gateway Multiple).
     *
     * @param elementId
     * @return MutableList<WfElementEntity>
     */
    private fun getArrowElements(elementId: String): MutableList<WfElementEntity> {
        return wfElementRepository.findAllArrowConnectorElement(elementId)
    }

    /**
     * Get Next Element Id.
     *
     * @param arrowElement
     * @return String
     */
    private fun getNextElementId(arrowElement: WfElementEntity): String {
        return wfElementDataRepository.findByElementAndAttributeId(arrowElement).attributeValue
    }

    /**
     * Get Element.
     *
     * @param elementId
     * @return WfElementEntity
     */
    private fun getElement(elementId: String): WfElementEntity {
        return wfElementRepository.findWfElementEntityByElementId(elementId)
    }

    /**
     * Pre Actions.
     *
     * @return MutableList<RestTemplateActionDto>
     */
    private fun preActions(instanceId: String?): MutableList<RestTemplateActionDto> {
        val preActions: MutableList<RestTemplateActionDto> = mutableListOf()

        if (this.isReviewAction(instanceId)) {
            preActions.add(
                RestTemplateActionDto(
                    name = "common.btn.review",
                    value = WfElementConstants.Action.REVIEW.value,
                    customYn = false
                )
            )
        }
        return preActions
    }

    /**
     * Review Action.
     */
    fun isReviewAction(instanceId: String?): Boolean {
        var isAction = false
        if (!instanceId.isNullOrBlank()) {
            val userKey = currentSessionUser.getUserKey()
            val instanceViewer = wfInstanceViewerRepository.getReviewYnByViewKey(instanceId, userKey)
            if (instanceViewer !== null && !instanceViewer.reviewYn) {
                isAction = true
            }
        }
        return isAction
    }

    /**
     * Post Actions.
     *
     * @param element
     * @return MutableList<RestTemplateActionDto>
     */
    private fun postActions(element: WfElementEntity): MutableList<RestTemplateActionDto> {

        val postActions: MutableList<RestTemplateActionDto> = mutableListOf()
        // 현재 element 속성에 회수, 반려가 존재할 경우
        element.elementDataEntities.forEach {
            if (it.attributeId == WfElementConstants.AttributeId.WITHDRAW.value &&
                it.attributeValue == WfElementConstants.AttributeValue.WITHDRAW_ENABLE.value
            ) {
                postActions.add(
                    RestTemplateActionDto(
                        name = "common.btn.withdraw",
                        value = WfElementConstants.Action.WITHDRAW.value,
                        customYn = false
                    )
                )
            }
            if (it.attributeId == WfElementConstants.AttributeId.REJECT_ID.value && it.attributeValue.trim().isNotEmpty()) {
                postActions.add(
                    RestTemplateActionDto(
                        name = "common.btn.reject",
                        value = WfElementConstants.Action.REJECT.value,
                        customYn = false
                    )
                )
            }
        }

        postActions.add(
            RestTemplateActionDto(
                name = "common.btn.cancel",
                value = WfElementConstants.Action.CANCEL.value,
                customYn = false
            )
        )
        postActions.add(
            RestTemplateActionDto(
                name = "common.btn.terminate",
                value = WfElementConstants.Action.TERMINATE.value,
                customYn = false
            )
        )
        postActions.add(
            RestTemplateActionDto(
                name = "common.btn.save",
                value = WfElementConstants.Action.SAVE.value,
                customYn = false
            )
        )
        logger.info("Make actions. {} ", postActions)

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
            WfElementConstants.ElementType.MANUAL_TASK.value -> {
                typeActions.addAll(makeAction(arrow.elementDataEntities))
            }
            WfElementConstants.ElementType.SCRIPT_TASK.value -> {
                typeActions.addAll(makeAction(arrow.elementDataEntities))
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                // condition-item 값에 따라 action인지 condition인지 확인하여 arrowConnector 정보를 리턴한다.
                val arrows =
                    when (nextElement.getElementDataValue(WfElementConstants.AttributeId.CONDITION_ITEM.value)
                            == WfElementConstants.AttributeValue.ACTION.value) {
                        true -> this.getArrowElements(nextElement.elementId)
                        false -> mutableListOf(arrow)
                    }
                arrows.forEach {
                    typeActions.addAll(makeAction(it.elementDataEntities))
                }
            }
        }
        if (typeActions.isEmpty()) {
            typeActions.add(
                RestTemplateActionDto(
                    name = "common.btn.process",
                    value = WfElementConstants.Action.PROGRESS.value,
                    customYn = false
                )
            )
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
            actionList.add(RestTemplateActionDto(name = actionName, value = actionValue, customYn = true))
        }
        return actionList
    }
}
