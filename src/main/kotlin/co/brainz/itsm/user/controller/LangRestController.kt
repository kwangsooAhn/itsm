package co.brainz.itsm.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.LocaleResolver
import java.util.Locale
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/rest/lang")
class LangRestController(private val localeResolver: LocaleResolver) {

    private val logger = LoggerFactory.getLogger(LangRestController::class.java)

    @PostMapping("/", "")
    fun updateLang(@RequestBody info: String, request: HttpServletRequest, response: HttpServletResponse) {
        val mapper = ObjectMapper()
        val result: MutableMap<*, *>? = mapper.readValue(info, MutableMap::class.java)
        val lang = result?.get("lang").toString()

        return localeResolver.setLocale(request, response, Locale(lang))
    }


}
