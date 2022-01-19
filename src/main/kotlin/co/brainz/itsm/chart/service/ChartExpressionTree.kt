package co.brainz.itsm.chart.service

import co.brainz.itsm.chart.dto.ChartConditionNode
import org.springframework.stereotype.Service

@Service
class ChartExpressionTree {
    var overallRoot: ChartConditionNode? = null

    fun execute(condition: String) {
        var str2 = ""
        var i = 0

        if (condition[0] == '-') {
            this.init(ChartConditionNode(data = "0"))
            i = 1
            this.add("-")
        } else {
            while (i < condition.length) {
                str2 += if (condition[i] in '0'..'9') condition[i] - '0' else break
                i++
            }
            this.init(ChartConditionNode(data = str2))
        }

        while (i < condition.length) {
            if (condition[i] in '0'..'9') {
                str2 = ""
                while (i < condition.length) {
                    str2 += if (condition[i] in '0'..'9') condition[i] - '0' else {
                        i--
                        break
                    }
                    i++
                }
                this.add(str2)
            } else if (condition[i] == '(')
                this.add("(")
            else if (condition[i] == ')')
                this.add(")")
            else if (condition[i] == '+')
                this.add("+")
            else if (condition[i] == '-')
                this.add("-")
            else if (condition[i] == '*')
                this.add("*")
            else if (condition[i] == '/')
                this.add("/")
            i++
        }

        println(this.print())
    }

    fun init(root: ChartConditionNode?) {
        overallRoot = root
    }

    private fun hasBracket(): Boolean {
        return hasBracket(overallRoot)
    }

    private fun hasBracket(root: ChartConditionNode?): Boolean {
        if (root == null || root.data.isEmpty()) return false
        if (root.data[0] == '(') return true
        return hasBracket(root.leftNode) || hasBracket(root.rightNode)
    }

    fun print(): Int {
        return print(overallRoot)
    }

    fun print(root: ChartConditionNode?): Int {
        if (root == null) return 0
        if (root.data[0] in '0'..'9') {
            return root.data.toInt()
        }
        if (root.data[0] == '+') {
            return print(root.leftNode) + print(root.rightNode)
        }
        if (root.data[0] == '-') {
            return print(root.leftNode) - print(root.rightNode)
        }
        if (root.data[0] == '*') {
            return print(root.leftNode) * print(root.rightNode)
        }
        if (root.data[0] == '/') {
            return print(root.leftNode) / print(root.rightNode)
        }
        return 0
    }

    fun add(value: String) {
        overallRoot = add(overallRoot, value)
    }

    private fun add(overallRoot: ChartConditionNode?, value: String): ChartConditionNode? {
        var root: ChartConditionNode? = overallRoot
        if (root == null || root.data.isEmpty()) {
            root = ChartConditionNode(data = value)
        } else if (root.data[0] == '(' && value[0] != ')') {
            root.leftNode = add(root.leftNode, value)
        } else if (root.data[0] == '(' && value[0] == ')') {
            if (!hasBracket(root.leftNode)) {
                var data = print(root.leftNode)
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
                root.leftNode = add(root.leftNode, value)
            }
        } else if (hasBracket(root)) {
            root.rightNode = add(root.rightNode, value)
        } else if (category(root.data[0]) < category(value[0])) {
            val node = ChartConditionNode(data = value)
            node.leftNode = root
            return node
        } else if (root.data[0] != '(' && category(root.data[0]) >= category(value[0]) && root.leftNode == null) {
            root.leftNode = add(root.leftNode, value)
        } else if (root.data[0] != '(' && category(root.data[0]) >= category(value[0]) && root.leftNode != null) {
            root.rightNode = add(root.rightNode, value)
        }
        return root
    }

    private fun category(c: Char): Int {
        if (c == '+') return 6
        if (c == '-') return 5
        if (c == '*') return 4
        if (c == '/') return 3
        if (c == '(' || c == ')') return 2
        if (c in '0'..'9') return 1
        return 0;
    }
}