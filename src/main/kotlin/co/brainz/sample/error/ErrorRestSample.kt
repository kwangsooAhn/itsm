package co.brainz.sample.error

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException

@RestController
@RequestMapping("exceptionrest")
class ErrorRestSample {
    @GetMapping("/io")
    fun io() {
        throw IOException("IO REST exception ~")
    }

    @GetMapping("/alice")
    fun alice() {
        throw AliceException(AliceErrorConstants.ERR, "Alice REST exception ~")
    }
}