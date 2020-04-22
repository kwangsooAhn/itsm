package co.brainz.framework.exception

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class AliceErrorHandler(
    errorAttributes: AliceErrorAttributes,
    errorViewResolvers: List<ErrorViewResolver>
) : BasicErrorController(errorAttributes, ErrorProperties(), errorViewResolvers) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @RequestMapping(produces = [MediaType.TEXT_HTML_VALUE])
    override fun errorHtml(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        logger.debug("Alice error handler for error html")
        return super.errorHtml(request, response)
    }

    @RequestMapping
    override fun error(request: HttpServletRequest): ResponseEntity<Map<String, Any>> {
        logger.debug("Alice error handler for responsebody")
        return super.error(request)
    }
}
