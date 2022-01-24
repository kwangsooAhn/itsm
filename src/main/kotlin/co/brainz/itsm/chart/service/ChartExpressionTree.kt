package co.brainz.itsm.chart.service

import co.brainz.itsm.chart.dto.ChartConditionNode
import org.springframework.stereotype.Service

@Service
class ChartExpressionTree {
    var root: ChartConditionNode? = null
    var condition = ""
    var index = 0
    var target = ""

    fun execute(condition: String): Int {
        // create root node
        this.initGlobalVariable(condition)
        this.initRoot()
        this.createExpressionTree()

        // create expression tree

        return this.calculateNode()
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
     * 최상단 루트 초기화 및 설정
     */
    private fun initRoot() {
        // 첫 문자에 음수가 오는 경우
        when (condition[0]) {
            '-' -> {
                root = ChartConditionNode(data = "0")
                index = 1
                this.addNode("-")
            }
            else -> {
                while (index < condition.length) {
                    if (condition[index] in '0'..'9') {
                        target += condition[index]
                    } else if (condition[index] == '[') {
                        for (innerIndex in index..condition.indices.last) {
                            if (condition[innerIndex] == ']') {
                                target = condition.substring(index, innerIndex + 1)
                                index = innerIndex
                                break;
                            }
                        }
                    } else {
                        break
                    }
                    index++
                }
                root = ChartConditionNode(data = target)
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
        if (targetNode == null || targetNode.data.isEmpty()) return false
        if (targetNode.data == "(") return true
        return hasParentheses(targetNode.leftNode) || hasParentheses(targetNode.rightNode)
    }

    fun calculateNode(): Int {
        return calculateNode(root)
    }

    fun calculateNode(targetNode: ChartConditionNode?): Int {
        var targetNodeData = targetNode?.data
        when (targetNode) {
            null -> return 0
            else -> {
                if (targetNode.data != "+" && targetNode.data != "-" && targetNode.data.matches(("[+-]?\\d*(\\.\\d+)?").toRegex())) {
                    return targetNode.data.toInt()
                }
                if (targetNode.data == "+") {
                    return calculateNode(targetNode.leftNode) + calculateNode(targetNode.rightNode)
                }
                if (targetNode.data == "-") {
                    return calculateNode(targetNode.leftNode) - calculateNode(targetNode.rightNode)
                }
                if (targetNode.data == "*") {
                    return calculateNode(targetNode.leftNode) * calculateNode(targetNode.rightNode)
                }
                if (targetNode.data == "/") {
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
        if (root == null || root.data.isEmpty()) {
            root = ChartConditionNode(data = target)
        }
        // root의 data는 "("이고 대상은 ")"이 아닌 경우
        else if (root.data == "(" && target != ")") {
            root.leftNode = addNode(root.leftNode, target)
        }
        // root의 data는 "("이고 대상은 ")"인 경우
        else if (root.data == "(" && target == ")") {
            // 해당 루트에 "("가 존재하지 않으면
            if (!hasParentheses(root.leftNode)) {
                var data = calculateNode(root.leftNode)
                if (data < 0) {
                    root.rightNode = ChartConditionNode(data = data.toString())
                    root.data = "+"
                    root.leftNode = ChartConditionNode(data = "0")
                } else {
                    root.data = data.toString()
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
        else if (getOperatorPrecedence(root.data) < getOperatorPrecedence(target)) {
            val node = ChartConditionNode(data = target)
            node.leftNode = root
            return node
        }
        // root.data는 "("가 아니고 root.data의 우선순위가 target의 우선순위보다 크거나 같은 경우
        else if (root.data != "(" && getOperatorPrecedence(root.data) >= getOperatorPrecedence(target)) {
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
        if (target == "&&") return 14
        if (target == "||") return 13
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
}