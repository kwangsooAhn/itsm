package co.brainz.sample.error

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.io.IOException

@Controller
@RequestMapping("/exception")
class ErrorSample {

    private val errorhandlePage: String = "sample/errorhandle"

    @GetMapping("/list")
    fun list(): String {
        return errorhandlePage
    }

    @GetMapping("/io")
    fun io() {
        throw IOException("IO exception ~")
    }

    @GetMapping("/alice")
    fun alice() {
        throw AliceException(AliceErrorConstants.ERR, "Alice exception ~")
    }
}