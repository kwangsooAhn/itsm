package co.brainz.framework.scheduling.task

import org.springframework.stereotype.Component
import java.util.*

@Component
class DeleteTempCIData : Runnable {

    private final var props: Properties = Properties();

    constructor()
    constructor(args: Any) {
        val params = args as List<*>
        props.setProperty("url", params[0].toString())
        props.setProperty("port", params[1].toString())
        props.setProperty("username", params[2].toString())
        props.setProperty("password", params[3].toString())
    }

    override fun run() {
        // args 체크
        println(props)

        // connection


        //임시테이블 삭제

        // close connection
    }

    /*private fun isValidArgs() {
        props.forEach { key, value ->
            if (value == "") {

            }
        }
    }*/


}
