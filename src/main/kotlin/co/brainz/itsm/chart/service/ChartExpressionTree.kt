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
        this.initCondition(condition)
        this.initRoot()
        this.createExpressionTree()

        // create expression tree

        return this.calculateNode()
    }

    /**
     * 조건문 초기화
     */
    private fun initCondition(condition: String) {
        this.condition = condition
    }

    /**
     * 최상단 루트 초기화 및 설정
     */
    private fun initRoot() {
        this.index = 0
        this.target = ""



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
                    this.addNode("+")
                }
                '-' -> {
                    this.addNode("-")
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
                    for (innerIndex in index .. condition.indices.last) {
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
        if (targetNode == null) return 0

        if (targetNode.data[0] in '0'..'9') {
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
/*
        if (targetNode.data == ">") {

        }
        if (targetNode.data == "<") {

        }
*/

        return 0
    }

    fun addNode(target: String) {
        root = addNode(root, target)
    }

    private fun addNode(overallRoot: ChartConditionNode?, target: String): ChartConditionNode? {
        var root: ChartConditionNode? = overallRoot
        if (root == null || root.data.isEmpty()) {
            root = ChartConditionNode(data = target)
        } else if (root.data == "(" && target != ")") {
            root.leftNode = addNode(root.leftNode, target)
        } else if (root.data == "(" && target == ")") {
            if (!hasParentheses(root.leftNode)) {
                var data = calculateNode(root.leftNode)
                if (data < 0) {
                    data *= -1
                    root.rightNode = ChartConditionNode(data = data.toString())
                    root.data = "-"
                    root.leftNode = null
                } else {
                    root.data = data.toString()
                    root.leftNode = null
                }
            } else {
                root.leftNode = addNode(root.leftNode, target)
            }
        } else if (hasParentheses(root)) {
            root.rightNode = addNode(root.rightNode, target)
        } else if (getOperatorPrecedence(root.data) < getOperatorPrecedence(target)) {
            val node = ChartConditionNode(data = target)
            node.leftNode = root
            return node
        } else if (root.data != "(" && getOperatorPrecedence(root.data) >= getOperatorPrecedence(target) && root.leftNode == null) {
            root.leftNode = addNode(root.leftNode, target)
        } else if (root.data != "(" && getOperatorPrecedence(root.data) >= getOperatorPrecedence(target) && root.leftNode != null) {
            root.rightNode = addNode(root.rightNode, target)
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