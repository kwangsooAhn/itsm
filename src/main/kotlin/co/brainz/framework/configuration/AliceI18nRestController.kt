package co.brainz.framework.configuration

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

/**
 * 자바스크립트 전용 다국어 클래스
 */
@RestController
@RequestMapping("/i18n")
class AliceI18nRestController(private val messageSource: MessageSource) {

     /**
     * 메시지를 받아서 다국어를 반환한다.
     */
    @GetMapping("/{messageId}")
    fun getMessage(@PathVariable messageId: String): String {
        return messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale())
    }
}