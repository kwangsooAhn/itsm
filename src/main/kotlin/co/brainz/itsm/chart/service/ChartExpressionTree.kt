package co.brainz.itsm.chart.service

import co.brainz.itsm.chart.constants.ChartConditionConstants
import co.brainz.itsm.chart.dto.ChartConditionNode
import co.brainz.itsm.chart.dto.ChartConditionNodeDataDto
import co.brainz.itsm.chart.dto.ChartTagInstanceDto
import org.springframework.stereotype.Service

@Service
class ChartExpressionTree {
    var root: ChartConditionNode? = null
    var condition = ""
    var index = 0
    var target = ""

    fun execute(condition: String, tagInstanceList: List<ChartTagInstanceDto>): List<ChartTagInstanceDto> {
        // create root node
        this.initGlobalVariable(condition)
        // init root
        this.initRoot()
        // create expression tree
        this.createExpressionTree()
        return this.calculate(tagInstanceList)
    }

    /**
     * 전역변수 초기화
     */
    private fun initGlobalVariable(condition: String) {
        this.root = null
        this.condition = condition
        this.index = 0
        this.target = ""
    }

    /**
     * 최상단 루트에 대한 설정 및 초기화를 진행한다.
     */
    private fun initRoot() {
        when (condition[0]) {
            // 음수인 경우
            '-' -> {
                var target = "-"
                root = ChartConditionNode(
                    data = ChartConditionNodeDataDto(
                        value = "0",
                        identifier = ChartConditionConstants.Identifier.LONG.value
                    )
                )
                this.addNode("+")
                for (innerIndex in index + 1..condition.indices.last) {
                    if (condition[innerIndex] in '0'..'9') {
                        target += condition[innerIndex]
                        index = innerIndex
                    } else {
                        break
                    }
                    index++
                }
                this.addNode(target)
            }
            else -> {
                while (index < condition.length) {
                    // 첫 문자가 숫자인 경우
                    if (condition[index] in '0'..'9') {
                        target += condition[index]
                    }
                    // 첫 문자가 괄호인 경우
                    else if (condition[index] == '[') {
                        for (innerIndex in index..condition.indices.last) {
                            if (condition[innerIndex] == ']') {
                                target = condition.substring(index, innerIndex + 1)
                                index = innerIndex
                                break
                            }
                        }
                    }
                    // 첫 문자가 쌍따옴표로 시작하는 경우
                    else if (condition[index] == '"') {
                        for (innerIndex in index + 1..condition.indices.last) {
                            if (condition[innerIndex] == '"') {
                                target = condition.substring(index, innerIndex + 1)
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
    private fun createExpressionTree() {
        while (index < condition.length) {
            when (condition[index]) {
                in '0'..'9' -> {
                    target = ""
                    while (index < condition.length) {
                        if (condition[index] in '0'..'9') {
                            target += condition[index]
                        } else {
                            index--
                            break
                        }
                        index++
                    }
                    this.addNode(target)
                }
                '"' -> {
                    for (innerIndex in index + 1..condition.indices.last) {
                        if (condition[innerIndex] == '"') {
                            val target = condition.substring(index..innerIndex)
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
                    if (condition[index - 1] == '(' ||
                        condition[index - 1] == '+' ||
                        condition[index - 1] == '-' ||
                        condition[index - 1] == '*' ||
                        condition[index - 1] == '/'
                    ) {
                        var target = "+"
                        for (innerIndex in index + 1..condition.indices.last) {
                            if (condition[innerIndex] in '0'..'9') {
                                target += condition[innerIndex]
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
                    if (condition[index - 1] == '(' ||
                        condition[index - 1] == '+' ||
                        condition[index - 1] == '-' ||
                        condition[index - 1] == '*' ||
                        condition[index - 1] == '/'
                    ) {
                        var target = "-"
                        for (innerIndex in index + 1..condition.indices.last) {
                            if (condition[innerIndex] in '0'..'9') {
                                target += condition[innerIndex]
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
                    if (condition[index + 1] == '=') {
                        this.addNode(">=")
                        index++
                    } else {
                        this.addNode(">")
                    }
                }
                '<' -> {
                    if (condition[index + 1] == '=') {
                        this.addNode("<=")
                        index++
                    } else {
                        this.addNode("<")
                    }
                }
                '!' -> {
                    if (condition[index + 1] == '=') {
                        this.addNode("!=")
                        index++
                    }
                }
                '=' -> {
                    if (condition[index + 1] == '=') {
                        this.addNode("==")
                        index++
                    }
                }
                '&' -> {
                    if (condition[index + 1] == '&') {
                        this.addNode("&&")
                        index++
                    }
                }
                '|' -> {
                    if (condition[index + 1] == '|') {
                        this.addNode("||")
                        index++
                    }
                }
                '[' -> {
                    for (innerIndex in index..condition.indices.last) {
                        if (condition[innerIndex] == ']') {
                            val target = condition.substring(index..innerIndex)
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

    private fun hasParentheses(targetNode: ChartConditionNode?): Boolean {
        if (targetNode == null || targetNode.data.value!!.isBlank()) return false
        if (targetNode.data.value == "(") return true
        return hasParentheses(targetNode.leftNode) || hasParentheses(targetNode.rightNode)
    }

    fun calculateNode(): Long {
        return calculateNode(root)
    }

    private fun calculate(tagInstanceList: List<ChartTagInstanceDto>): List<ChartTagInstanceDto> {
        when (root?.data?.identifier) {
            ChartConditionConstants.Identifier.LOGICAL.value -> {

            }
            ChartConditionConstants.Identifier.COMPARISON.value -> {
                return tagInstanceList
            }
            ChartConditionConstants.Identifier.ARITHMETIC.value -> {
               return tagInstanceList
            }
            // 문자나 숫자가 오는 경우는 그대로 리턴해준다.
            ChartConditionConstants.Identifier.LONG.value, ChartConditionConstants.Identifier.STRING.value -> {

            }
            ChartConditionConstants.Identifier.PARENTHESES.value -> {

            }
            ChartConditionConstants.Identifier.TAG.value -> {

            }
            else -> {

            }
        }
        return tagInstanceList
    }

    private fun arithmetic(targetNode: ChartConditionNode?): Long {
        when (targetNode) {
            null -> return 0
            else -> {
                if (targetNode.data.value != "+" && targetNode.data.value != "-" && targetNode.data.value!!.matches(("[+-]?\\d*(\\.\\d+)?").toRegex())) {
                    return targetNode.data.value!!.toLong()
                }
                if (targetNode.data.value == "+") {
                    return arithmetic(targetNode.leftNode) + arithmetic(targetNode.rightNode)
                }
                if (targetNode.data.value == "-") {
                    return arithmetic(targetNode.leftNode) - arithmetic(targetNode.rightNode)
                }
                if (targetNode.data.value == "*") {
                    return arithmetic(targetNode.leftNode) * arithmetic(targetNode.rightNode)
                }
                if (targetNode.data.value == "/") {
                    return arithmetic(targetNode.leftNode) / arithmetic(targetNode.rightNode)
                }
                return 0
            }
        }
    }

    private fun calculateNode(targetNode: ChartConditionNode?): Long {
        when (targetNode) {
            null -> return 0
            else -> {
                if (targetNode.data.value != "+" && targetNode.data.value != "-" && targetNode.data.value!!.matches(("[+-]?\\d*(\\.\\d+)?").toRegex())) {
                    return targetNode.data.value!!.toLong()
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
            if (!hasParentheses(root.leftNode)) {
                var data = calculateNode(root.leftNode)
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
                }
            } else {
                root.leftNode = addNode(root.leftNode, target)
            }
        }
        // root에 "("가 존재하고 있는 경우
        else if (hasParentheses(root)) {
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
        return if (target.matches(("[+-]?\\d*(\\.\\d+)?").toRegex())) {
            ChartConditionConstants.Identifier.LONG.value
        }
        // Tag / 태그
        else if (target.startsWith(ChartConditionConstants.Parentheses.PREFIX_SQUARE_BRACKETS.value) &&
            target.endsWith(ChartConditionConstants.Parentheses.SUFFIX_SQUARE_BRACKETS.value)
        ) {
            ChartConditionConstants.Identifier.TAG.value
        }
        // String / 문자열
        else {
            ChartConditionConstants.Identifier.STRING.value
        }
    }
}

