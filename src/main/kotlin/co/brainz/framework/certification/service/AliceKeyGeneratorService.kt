package co.brainz.framework.certification.service

import java.util.Random

class AliceKeyGeneratorService {

    private var lowerCheck: Boolean = false
    var size: Int = 0

    private val zeroToLowerCaseZ = 75 // 0~z 까지의 갯수
    private val charCodeZero = 48 // 0
    private val charCodeNine = 57 // 9
    private val charCodeUpperA = 65 // A
    private val charCodeUpperZ = 90 // Z
    private val charCodeLowerCaseA = 97 // a
    private val charCodeLowerCaseZ = 122 // z

    fun getKey(size: Int, lowerCheck: Boolean): String {
        this.size = size
        this.lowerCheck = lowerCheck
        return init()
    }

    private fun init(): String {
        val random = Random()
        val sb = StringBuffer()

        var num: Int
        do {
            num = random.nextInt(zeroToLowerCaseZ) + charCodeZero
            if ((num in charCodeZero..charCodeNine) || (num in charCodeUpperA..charCodeUpperZ) || (num in charCodeLowerCaseA..charCodeLowerCaseZ)) {
                sb.append(num.toChar())
            } else {
                continue
            }
        } while (sb.length < size)

        if (lowerCheck) {
            return sb.toString().toLowerCase()
        }
        return sb.toString()
    }
}
