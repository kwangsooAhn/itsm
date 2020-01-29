package co.brainz.framework.configuration

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

@RestController
@RequestMapping("/i18n")
class AliceI18nRestController(private val messageSource: MessageSource) {

    @GetMapping("/{messageId}")
    fun i18n(@PathVariable messageId: String): String {
        return messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale())
    }
}