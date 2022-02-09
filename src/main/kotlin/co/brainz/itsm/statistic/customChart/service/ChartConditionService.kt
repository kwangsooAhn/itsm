/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service

import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.itsm.statistic.customChart.constants.ChartConditionConstants
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import org.slf4j.LoggerFactory
import org.springframework.expression.EvaluationContext
import org.springframework.expression.Expression
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Service

@Service
class ChartConditionService(
    private val tagRepository: AliceTagRepository,
    private val chartManagerService: ChartManagerService,
    private val wfTokenDataRepository: WfTokenDataRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 조건에 일치하는 인스턴스 리스트를 리턴한다.
     */
    fun getChartConditionByTagInstance(
        chartCondition: String?,
        instanceList: List<WfInstanceEntity>
    ): List<WfInstanceEntity> {
        return if (!chartCondition.isNullOrBlank() && instanceList.isNotEmpty()) {
            // 1. 조건식(chartCondition)에서 대괄호("[","]")를 제외한 태그만 추출한다.
            val tags = this.getTagsInCondition(chartCondition)
            if (tags.isNotEmpty()) {
                // 2. 위에서 추출한 태그를 모두 포함하고 있는 인스턴스를 추출한다.
                val targetInstanceList = this.getInstanceListIncludeTags(instanceList, tags)
                // 3. 조건문에 통과되는 인스턴스만 모두 추출한다.
                this.getTagInstanceList(chartCondition, targetInstanceList, tags)
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    /**
     * 인스턴스가 조건문과 일치하는지 판별한다.
     */
    private fun getTagInstanceList(
        chartCondition: String,
        targetInstanceList: List<WfInstanceEntity>,
        tags: LinkedHashSet<String>
    ): List<WfInstanceEntity> {
        val instanceList = mutableListOf<WfInstanceEntity>()
        targetInstanceList.forEach { targetInstance ->
            if (this.conditionDiscrimination(chartCondition, targetInstance, tags)) {
                instanceList.add(targetInstance)
            }
        }

        return instanceList
    }

    /**
     * 조건식이 타당한지 판별하고 조건식에 타당하면 리턴한다.
     */
    private fun conditionDiscrimination(
        chartCondition: String,
        instance: WfInstanceEntity,
        tags: LinkedHashSet<String>
    ): Boolean {
        // 태그가 달린 컴포넌트의 최신 값을 가져온다.
        val tagDataMap = this.getConditionTagValue(instance, tags)
        return if (tagDataMap.isNotEmpty()) {
            val context: EvaluationContext = StandardEvaluationContext(ChartConditionDateTimeUtil())
            val chartCondition = this.replaceConditionTagValue(chartCondition, tagDataMap)
            val parser: ExpressionParser = SpelExpressionParser()
            try {
                val discriminant: Expression = parser.parseExpression(chartCondition)
                discriminant.getValue(context) as Boolean
            } catch (e: Exception) {
                false
            }
        } else {
            false
        }
    }

    /**
     * 사용자가 설정한 태그를 모두 포함하고 있는 인스턴스의 리스트만 가져온다.
     */
    private fun getInstanceListIncludeTags(
        instanceList: List<WfInstanceEntity>,
        tags: LinkedHashSet<String>
    ): List<WfInstanceEntity> {
        val targetInstanceList = mutableListOf<WfInstanceEntity>()
        instanceList.forEach { instance ->
            // "대상 태그"를 포함하고 있는 인스턴스 중에서 tagSet에 담겨있는 "조건 태그"를 모두 포함하고 있는 인스턴스를 수집.
            val targetTagSet = LinkedHashSet<String>()
            val componentIds = LinkedHashSet<String>()
            // 해당 인스턴스의 컴포넌트 아이디 수집
            instance.document.form.components.forEach { wfComponentEntity ->
                componentIds.add(wfComponentEntity.componentId)
            }
            // 위에서 수집한 컴포넌트 아이디를 사용하여 awf_tag 테이블의 tag 데이터를 가져온다.
            val targetTags =
                tagRepository.findByTargetIds(AliceTagConstants.TagType.COMPONENT.code, componentIds)
            targetTags.forEach { tag ->
                targetTagSet.add(tag.tagValue)
            }

            if (targetTagSet.containsAll(tags)) {
                targetInstanceList.add(instance)
            }
        }

        return targetInstanceList
    }

    /**
     * 조건문(chartCondition)에서 태그 데이터를 추출한다.
     */
    private fun getTagsInCondition(chartCondition: String): LinkedHashSet<String> {
        val chartConditionTags = LinkedHashSet<String>()
        val returnSet = LinkedHashSet<String>()
        var startIndex = 0

        while (startIndex < chartCondition.length) {
            if (chartCondition[startIndex].toString() == ChartConditionConstants.Parentheses.PREFIX_SQUARE_BRACKETS.value) {
                for (index in startIndex + 1..chartCondition.indices.last) {
                    if (chartCondition[index].toString() == ChartConditionConstants.Parentheses.SUFFIX_SQUARE_BRACKETS.value) {
                        var tag = chartCondition.substring(startIndex + 1, index)
                        chartConditionTags.add(tag)
                        startIndex = index
                        break
                    }
                }
            }
            startIndex++
        }

        return returnSet
    }

    /**
     * 인스턴스의 조건 태그 값을 구한다.
     */
    private fun getConditionTagValue(
        instance: WfInstanceEntity,
        tags: LinkedHashSet<String>
    ): LinkedHashMap<String, String> {
        // 컴포넌트 타입의 태그에 대한 수집을 진행한다.
        val componentTagList = chartManagerService.getTagValueList(
            AliceTagConstants.TagType.COMPONENT.code,
            tags.toList()
        )

        // 인스턴스의 마지막 토큰을 수집한다.
        val lastToken = instance.tokens?.let {
            it.last()
        }
        // 위에서 수집한 마지막 토큰을 가지고
        // wf_token_data 테이블에 접근하여 해당 컴포넌트의 최신 값을 추출한다.
        // 이때 LinkedHashMap에 데이터를 tagValue : value 형태로 담는다
        // tagValue의 경우 중복이 발생할 수 있는데, 이 경우 가장 첫 번째로 입력되는 데이터만 사용한다. (기술적 한계)
        var tagDataMap = LinkedHashMap<String, String>()
        if (lastToken != null) {
            val lastTokenData = wfTokenDataRepository.findWfTokenDataEntitiesByTokenTokenId(lastToken.tokenId)
            lastTokenData.forEach { wfTokenDataEntity ->
                componentTagList.forEach { componentTag ->
                    if (wfTokenDataEntity.component.componentId == componentTag.targetId) {
                        if (tagDataMap[componentTag.tagValue] == null) {
                            var tagKey =
                                ChartConditionConstants.Parentheses.PREFIX_SQUARE_BRACKETS.value + componentTag.tagValue +
                                        ChartConditionConstants.Parentheses.SUFFIX_SQUARE_BRACKETS.value
                            tagDataMap[tagKey] = this.replaceTagDataFormat(wfTokenDataEntity.value)
                        }
                    }
                }
            }
        }

        return tagDataMap
    }

    /**
     * 조건문에서 태그 값을 컴포넌트의 값으로 치환한다.
     */
    private fun replaceConditionTagValue(
        chartCondition: String,
        tagDataMap: LinkedHashMap<String, String>
    ): String {
        var targetCondition = ""
        tagDataMap.forEach { tagData ->
            var value = ""
            value = if (targetCondition.isBlank()) {
                chartCondition
            } else {
                targetCondition
            }
            targetCondition = value.replace(tagData.key, tagData.value)
        }

        return targetCondition
    }


    /**
     * 해당 값의 숫자, 문자, 날짜에 따라 각 형식에 맞게 리턴하도록 한다.
     */
    private fun replaceTagDataFormat(value: String): String {
        // 숫자
        return if (value.matches(("^[+-]?\\d*(\\.?\\d*)\$").toRegex())) {
            value
        } else {
            // 문자열 혹은 날짜
            "\'" + value + "\'"
        }
    }
}
