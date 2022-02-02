package co.brainz.itsm.statistic.customChart.service

import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.itsm.statistic.customChart.constants.ChartConditionConstants
import co.brainz.itsm.statistic.customChart.dto.ChartConditionNode
import co.brainz.itsm.statistic.customChart.dto.ChartConditionNodeDataDto
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.dto.ChartTagInstanceDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.stereotype.Service




@Service
class ChartExpressionTree(
    private val tagRepository: AliceTagRepository,
    private val chartManagerService: ChartManagerService,
    private val wfTokenDataRepository: WfTokenDataRepository
) {
    var root: ChartConditionNode? = null
    var chartCondition = ""
    var index = 0
    var target = ""
    var tags = linkedSetOf<String>()

    fun execute(chartDto: ChartDto, tagInstanceList: List<ChartTagInstanceDto>): List<ChartTagInstanceDto> {
        this.initGlobalVariable(chartDto.chartCondition)
        val targetInstanceList = this.getInstanceListIncludeTags(tagInstanceList)
        return if (chartCondition.isNotBlank() && targetInstanceList.isNotEmpty()) {
            this.getTagInstanceList(targetInstanceList)
        } else {
            tagInstanceList
        }
    }

    /**
     * 전역변수 초기화
     */
    private fun initGlobalVariable(chartCondition: String) {
        this.root = null
        this.chartCondition = chartCondition
        this.index = 0
        this.target = ""
        this.tags = LinkedHashSet()
    }

    /**
     * 최상단 루트에 대한 설정 및 초기화를 진행한다.
     */
    private fun initRoot(chartCondition: String) {
        when (chartCondition[0]) {
            // 첫 문자가 음의 부호 및 양의 부호인 경우
            '-', '+' -> {
                var target = ""
                target = if (chartCondition[0] == '-') {
                    "-"
                } else {
                    "+"
                }
                root = ChartConditionNode(
                    data = ChartConditionNodeDataDto(
                        value = "0",
                        identifier = ChartConditionConstants.Identifier.LONG.value
                    )
                )
                this.addNode("+")
                for (innerIndex in index + 1..chartCondition.indices.last) {
                    if (chartCondition[innerIndex] in '0'..'9') {
                        target += chartCondition[innerIndex]
                        index = innerIndex
                    } else {
                        break
                    }
                    index++
                }
                this.addNode(target)
            }
            else -> {
                while (index < chartCondition.length) {
                    // 첫 문자가 숫자인 경우
                    if (chartCondition[index] in '0'..'9') {
                        target += chartCondition[index]
                    }
                    // 첫 문자가 괄호인 경우
                    else if (chartCondition[index] == '[') {
                        for (innerIndex in index..chartCondition.indices.last) {
                            if (chartCondition[innerIndex] == ']') {
                                target = chartCondition.substring(index, innerIndex + 1)
                                index = innerIndex
                                break
                            }
                        }
                    }
                    // 첫 문자가 쌍따옴표로 시작하는 경우
                    else if (chartCondition[index] == '"') {
                        for (innerIndex in index + 1..chartCondition.indices.last) {
                            if (chartCondition[innerIndex] == '"') {
                                target = chartCondition.substring(index, innerIndex + 1)
                                index = innerIndex
                                break
                            }
                        }
                    } else {
                        break
                    }
                    index++
                }
                root = ChartConditionNode(
                    data = ChartConditionNodeDataDto(
                        value = target,
                        identifier = this.getIdentifier(target)
                    )
                )
            }
        }
    }

    /**
     * 수식 트리 (Expression Tree) 생성 진행
     */
    private fun createExpressionTree(chartCondition: String) {
        while (index < chartCondition.length) {
            when (chartCondition[index]) {
                in '0'..'9' -> {
                    target = ""
                    while (index < chartCondition.length) {
                        if (chartCondition[index] in '0'..'9') {
                            target += chartCondition[index]
                        } else {
                            index--
                            break
                        }
                        index++
                    }
                    this.addNode(target)
                }
                '"' -> {
                    for (innerIndex in index + 1..chartCondition.indices.last) {
                        if (chartCondition[innerIndex] == '"') {
                            val target = chartCondition.substring(index..innerIndex)
                            this.addNode(target)
                            index = innerIndex
                            break
                        }
                    }
                }
                '(' -> {
                    this.addNode("(")
                }
                ')' -> {
                    this.addNode(")")
                }
                '+' -> {
                    if (chartCondition[index - 1] == '(' ||
                        chartCondition[index - 1] == '+' ||
                        chartCondition[index - 1] == '-' ||
                        chartCondition[index - 1] == '*' ||
                        chartCondition[index - 1] == '/'
                    ) {
                        var target = "+"
                        for (innerIndex in index + 1..chartCondition.indices.last) {
                            if (chartCondition[innerIndex] in '0'..'9') {
                                target += chartCondition[innerIndex]
                                index = innerIndex
                            } else {
                                index = innerIndex - 1
                                break
                            }
                        }
                        this.addNode(target)
                    }
                    this.addNode("+")
                }
                '-' -> {
                    if (chartCondition[index - 1] == '(' ||
                        chartCondition[index - 1] == '+' ||
                        chartCondition[index - 1] == '-' ||
                        chartCondition[index - 1] == '*' ||
                        chartCondition[index - 1] == '/'
                    ) {
                        var target = "-"
                        for (innerIndex in index + 1..chartCondition.indices.last) {
                            if (chartCondition[innerIndex] in '0'..'9') {
                                target += chartCondition[innerIndex]
                                index = innerIndex
                            } else {
                                index = innerIndex - 1
                                break
                            }
                        }
                        this.addNode(target)
                    } else {
                        this.addNode("-")
                    }
                }
                '*' -> {
                    this.addNode("*")
                }
                '/' -> {
                    this.addNode("/")
                }
                '>' -> {
                    if (chartCondition[index + 1] == '=') {
                        this.addNode(">=")
                        index++
                    } else {
                        this.addNode(">")
                    }
                }
                '<' -> {
                    if (chartCondition[index + 1] == '=') {
                        this.addNode("<=")
                        index++
                    } else {
                        this.addNode("<")
                    }
                }
                '!' -> {
                    if (chartCondition[index + 1] == '=') {
                        this.addNode("!=")
                        index++
                    }
                }
                '=' -> {
                    if (chartCondition[index + 1] == '=') {
                        this.addNode("==")
                        index++
                    }
                }
                '&' -> {
                    if (chartCondition[index + 1] == '&') {
                        this.addNode("&&")
                        index++
                    }
                }
                '|' -> {
                    if (chartCondition[index + 1] == '|') {
                        this.addNode("||")
                        index++
                    }
                }
                '[' -> {
                    for (innerIndex in index..chartCondition.indices.last) {
                        if (chartCondition[innerIndex] == ']') {
                            val target = chartCondition.substring(index..innerIndex)
                            this.addNode(target)
                            index = innerIndex
                            break
                        }
                    }
                }
            }
            index++
        }
    }

    private fun hasPrefixParentheses(root: ChartConditionNode?): Boolean {
        if (root == null || root.data.value == null) return false
        if (root.data.value == "(") return true
        return hasPrefixParentheses(root.leftNode) || hasPrefixParentheses(root.rightNode)
    }

    private fun getTagInstanceList(
        tagInstanceList: List<ChartTagInstanceDto>
    ): List<ChartTagInstanceDto> {
        val instanceList = mutableListOf<WfInstanceEntity>()
        tagInstanceList.forEach { chartTagInstanceDto ->
            chartTagInstanceDto.conditionInstances.forEach { conditionInstance ->
                if (this.chartConditionDiscrimination(conditionInstance)) {
                    instanceList.add(conditionInstance)
                }
            }
            chartTagInstanceDto.conditionInstances = instanceList
        }

        return tagInstanceList
    }

    /**
     * 조건식이 타당한지 판별하고 조건식에 타당하면 리턴한다.
     */
    private fun chartConditionDiscrimination(instance: WfInstanceEntity): Boolean {
        // 태그가 달린 컴포넌트의 최신 값을 가져온다.
        val tagDataMap = this.getConditionTagValue(instance)
        return if (tagDataMap.isNotEmpty()) {
            val condition = this.replaceTagValueWithComponentValueInCondition(tagDataMap)
            this.initRoot(condition)
            this.createExpressionTree(condition)
            this.initGlobalVariable(chartCondition)
            val parser: ExpressionParser = SpelExpressionParser()
            try {
                val exp: org.springframework.expression.Expression = parser.parseExpression("'string'=='string'")
                val message = exp.value as Boolean
                val test = message
            } catch (e: Exception) {
                false
            }
            true
        } else {
            false
        }
        return true
    }

/*    private fun isFit(root: ChartConditionNode): ChartConditionNode {
        var isFit = false
        when (root.data.identifier) {
            ChartConditionConstants.Identifier.LOGICAL.value -> {
                val test = root.leftNode?.data!!.value!!.toString().toLong() > root.rightNode?.data!!.value!!.toString()
                    .toLong()
                return ChartConditionNode(
                    data = ChartConditionNodeDataDto(
                        value = test,
                        identifier = this.getIdentifier(test)
                    )
                )
            }
            ChartConditionConstants.Identifier.COMPARISON.value -> {

            }
            ChartConditionConstants.Identifier.ARITHMETIC.value -> {

            }
            ChartConditionConstants.Identifier.LONG.value -> {

            }
            ChartConditionConstants.Identifier.STRING.value -> {

            }
            else -> return false
        }
    }*/

/*
    private fun calculateNode(targetNode: ChartConditionNode?): Long {
        when (targetNode) {
            null -> return 0
            else -> {
                if (targetNode.data.value != "+" && targetNode.data.value != "-" && targetNode.data.value!!.matches(("[+-]?\\d*(\\.\\d+)?").toRegex())) {
                    return targetNode.data.value!!.toString().toLong()
                }
                if (targetNode.data.value == "+") {
                    return calculateNode(targetNode.leftNode) + calculateNode(targetNode.rightNode)
                }
                if (targetNode.data.value == "-") {
                    return calculateNode(targetNode.leftNode) - calculateNode(targetNode.rightNode)
                }
                if (targetNode.data.value == "*") {
                    return calculateNode(targetNode.leftNode) * calculateNode(targetNode.rightNode)
                }
                if (targetNode.data.value == "/") {
                    return calculateNode(targetNode.leftNode) / calculateNode(targetNode.rightNode)
                }
                return 0
            }
        }
    }
*/

    private fun calculateNode(root: ChartConditionNode): Any {
        var isFit = false
        if (root.data.identifier == ChartConditionConstants.Identifier.COMPARISON.value) {
            this.comparison(root)
        }
        if (root.data.identifier == ChartConditionConstants.Identifier.ARITHMETIC.value) {
            this.arithmetic(root)
        }
        if (root.data.identifier == ChartConditionConstants.Identifier.STRING.value) {
            return root.data.value.toString()
        }
        if (root.data.identifier == ChartConditionConstants.Identifier.LONG.value) {
            return root.data.value!!.toLong()
        }
        return isFit
    }

    fun comparison(target: ChartConditionNode): Any {
        when (target.data.identifier) {
            ">" -> {
                // leftNode, rightNode not Null
                if (target.leftNode != null && target.rightNode != null) {
                    // 같은 식별자인 경우
                    if (target.leftNode?.data?.identifier == target.rightNode?.data?.identifier) {
                        // 문자일 때
                        if (target.leftNode?.data?.identifier == ChartConditionConstants.Identifier.STRING.value) {
                            return target?.leftNode?.data?.value!! > target?.rightNode?.data?.value!!
                            // 숫자일 때
                        } else if (target.leftNode?.data?.identifier == ChartConditionConstants.Identifier.LONG.value) {
                            return target?.leftNode?.data?.value!!.toLong() > target?.rightNode?.data?.value!!.toLong()
                        }
                    } else if (target.leftNode?.data?.identifier != ChartConditionConstants.Identifier.STRING.value &&
                        target.leftNode?.data?.identifier != ChartConditionConstants.Identifier.LONG.value
                    ) {
                        return calculateNode(target.leftNode!!)
                    } else if (target.rightNode?.data?.identifier != ChartConditionConstants.Identifier.STRING.value &&
                            target.rightNode?.data?.identifier != ChartConditionConstants.Identifier.LONG.value
                    ) {
                        return calculateNode(target.rightNode!!)
                    }
                }
            }
        }
        return 0
    }

    fun arithmetic(target: ChartConditionNode) {
    }

    fun addNode(target: String) {
        root = addNode(root, target)
    }

    private fun addNode(overallRoot: ChartConditionNode?, target: String): ChartConditionNode? {
        var root: ChartConditionNode? = overallRoot
        // root가 null이거나 root의 데이터가 비어있는 경우
        if (root == null || root.data.value!!.isEmpty()) {
            root = ChartConditionNode(
                data = ChartConditionNodeDataDto(
                    value = target,
                    identifier = this.getIdentifier(target)
                )
            )
        }
        // root의 data는 "("이고 대상은 ")"이 아닌 경우
        else if (root.data.value == "(" && target != ")") {
            root.leftNode = addNode(root.leftNode, target)
        }
        // root의 data는 "("이고 대상은 ")"인 경우
        else if (root.data.value == "(" && target == ")") {
            // 해당 루트에 "("가 존재하지 않으면
            if (!hasPrefixParentheses(root.leftNode)) {
               /* var data = calculateNode(root.leftNode)
                if (data < 0) {
                    root.rightNode = ChartConditionNode(
                        data = ChartConditionNodeDataDto(
                            value = data.toString(),
                            identifier = this.getIdentifier(data.toString())
                        )
                    )
                    root.data.value = "+"
                    root.data.identifier = this.getIdentifier(ChartConditionConstants.Identifier.ARITHMETIC.value)
                    root.leftNode = ChartConditionNode(
                        data = ChartConditionNodeDataDto(
                            value = "0",
                            identifier = ChartConditionConstants.Identifier.LONG.value
                        )
                    )
                } else {
                    root.data.value = data.toString()
                    root.data.identifier = this.getIdentifier(data.toString())
                    root.leftNode = null
                }*/
            } else {
                root.leftNode = addNode(root.leftNode, target)
            }
        }
        // root에 "("가 존재하고 있는 경우
        else if (hasPrefixParentheses(root)) {
            root.rightNode = addNode(root.rightNode, target)
        }
        // root.data의 우선순위가 target의 우선순위보다 작은 경우
        else if (getOperatorPrecedence(root.data.value!!) < getOperatorPrecedence(target)) {
            val node = ChartConditionNode(
                data = ChartConditionNodeDataDto(
                    value = target,
                    identifier = this.getIdentifier(target)
                )
            )
            node.leftNode = root
            return node
        }
        // root.data는 "("가 아니고 root.data의 우선순위가 target의 우선순위보다 크거나 같은 경우
        else if (root.data.value != "(" && getOperatorPrecedence(root.data.value!!) >= getOperatorPrecedence(target)) {
            if (root.leftNode == null) {
                root.leftNode = addNode(root.leftNode, target)
            } else {
                root.rightNode = addNode(root.rightNode, target)
            }
        }

        return root
        var test = "a" <= "b"
    }

    /**
     * 연산자 우선순쉬
     */
    private fun getOperatorPrecedence(target: String): Int {
        if (target == "||") return 14
        if (target == "&&") return 13
        if (target == "!=") return 12
        if (target == "==") return 11
        if (target == ">=") return 10
        if (target == "<=") return 9
        if (target == ">") return 8
        if (target == "<") return 7
        if (target == "+") return 6
        if (target == "-") return 5
        if (target == "*") return 4
        if (target == "/") return 3
        if (target == "(" || target == ")") return 2
        if (target in "0".."9") return 1
        return 0;
    }

    /**
     * 대상 식별자에 대한 분리를 진행한다
     */
    private fun getIdentifier(target: String): String? {
        // blank
        if (target.isBlank()) {
            return null
        }
        // Logical Operator / 논리 연산자
        for (data in ChartConditionConstants.Logical.values()) {
            if (target == data.operator) {
                return ChartConditionConstants.Identifier.LOGICAL.value
            }
        }
        // Comparison Operator / 비교 연산자
        for (data in ChartConditionConstants.Comparison.values()) {
            if (target == data.operator) {
                return ChartConditionConstants.Identifier.COMPARISON.value
            }
        }
        // Arithmetic Operator / 산술 연산자
        for (data in ChartConditionConstants.Arithmetic.values()) {
            if (target == data.operator) {
                return ChartConditionConstants.Identifier.ARITHMETIC.value
            }
        }
        // Parentheses / 괄호
        for (data in ChartConditionConstants.Parentheses.values()) {
            if (target == data.value) {
                return ChartConditionConstants.Identifier.PARENTHESES.value
            }
        }
        // Number(Long Type) / 숫자
        return if (target != "+" && target != "-" && target.matches(("[+-]?\\d*(\\.\\d+)?").toRegex())) {
            ChartConditionConstants.Identifier.LONG.value
        }
        // Tag / 태그
        else if (target.startsWith(ChartConditionConstants.Parentheses.PREFIX_SQUARE_BRACKETS.value) &&
            target.endsWith(ChartConditionConstants.Parentheses.SUFFIX_SQUARE_BRACKETS.value)
        ) {
            ChartConditionConstants.Identifier.TAG.value
        }
        // String / 문자열
        else if (target.startsWith("\"") && target.endsWith("\"")) {
            ChartConditionConstants.Identifier.STRING.value
        } else {
            ChartConditionConstants.Identifier.BOOLEAN.value
        }
    }

    /**
     * 사용자가 설정한 태그를 모두 포함하고 있는 인스턴스의 리스트만 가져온다.
     */
    private fun getInstanceListIncludeTags(
        tagInstanceList: List<ChartTagInstanceDto>
    ): List<ChartTagInstanceDto> {
        tags = this.getTagsInCondition(chartCondition)
        return if (tags.isNotEmpty()) {
            tagInstanceList.forEach { chartTagInstanceDto ->
                val instanceList = mutableListOf<WfInstanceEntity>()
                chartTagInstanceDto.instances.forEach { wfInstanceEntity ->
                    // "대상 태그"를 포함하고 있는 인스턴스 중에서 tagSet에 담겨있는 "조건 태그"를 모두 포함하고 있는 인스턴스를 수집.
                    val targetTagSet = LinkedHashSet<String>()
                    val componentIds = LinkedHashSet<String>()
                    // 해당 인스턴스의 컴포넌트 아이디 수집
                    wfInstanceEntity.document.form.components.forEach { wfComponentEntity ->
                        componentIds.add(wfComponentEntity.componentId)
                    }
                    // 위에서 수집한 컴포넌트 아이디를 사용하여 awf_tag 테이블의 tag 데이터를 가져온다.
                    val targetTags =
                        tagRepository.findByTargetIds(AliceTagConstants.TagType.COMPONENT.code, componentIds)
                    targetTags.forEach { tag ->
                        targetTagSet.add(tag.tagValue)
                    }

                    if (targetTagSet.containsAll(tags)) {
                        instanceList.add(wfInstanceEntity)
                    }
                }
                chartTagInstanceDto.conditionInstances = instanceList
            }
            tagInstanceList
        } else {
            tagInstanceList
        }
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
                        var tag = chartCondition.substring(startIndex, index + 1)
                        chartConditionTags.add(tag)
                        startIndex = index
                        break
                    }
                }
            }
            startIndex++
        }

        if (chartConditionTags.isNotEmpty()) {
            chartConditionTags.forEach { chartConditionTag ->
                returnSet.add(
                    chartConditionTag.removeSurrounding(
                        ChartConditionConstants.Parentheses.PREFIX_SQUARE_BRACKETS.value,
                        ChartConditionConstants.Parentheses.SUFFIX_SQUARE_BRACKETS.value
                    )
                )
            }
        }

        return returnSet
    }

    /**
     * 인스턴스의 조건 태그 값을 구한다.
     */
    private fun getConditionTagValue(instance: WfInstanceEntity): LinkedHashMap<String, String> {
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
                                ChartConditionConstants.Parentheses.PREFIX_SQUARE_BRACKETS.value + componentTag.tagValue + ChartConditionConstants.Parentheses.SUFFIX_SQUARE_BRACKETS.value
                            tagDataMap[tagKey] = wfTokenDataEntity.value
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
    private fun replaceTagValueWithComponentValueInCondition(
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
}

