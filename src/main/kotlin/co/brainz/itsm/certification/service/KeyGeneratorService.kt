package co.brainz.itsm.certification.service

import java.util.Random

class KeyGeneratorService {

    private var lowerCheck: Boolean = false
    var size: Int = 0

    fun getKey(size:Int, lowerCheck: Boolean): String {
        this.size = size
        this.lowerCheck = lowerCheck
        return init()
    }

    private fun init(): String {
        val random: Random = Random()
        val sb: StringBuffer = StringBuffer()

        var num: Int = 0
        do {
            num = random.nextInt(75) + 48
            if ((num in 48..57) || (num in 65..90) || (num in 97..122)) {
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
