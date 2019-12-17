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

    @GetMapping("/list")
    fun list(): String {
        return "sample/errorhandle"
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