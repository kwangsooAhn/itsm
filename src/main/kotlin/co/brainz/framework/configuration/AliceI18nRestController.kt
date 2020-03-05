package co.brainz.framework.configuration

import com.google.gson.JsonObject
import java.util.ResourceBundle
import net.rakugakibox.util.YamlResourceBundle
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * 자바스크립트 전용 다국어 클래스
 */
@RestController
@RequestMapping("/i18n")
class AliceI18nRestController(private val messageSource: MessageSource) {

    @Value("\${spring.messages.basename}")
    private var basename = ""

    /**
     * 전체 메시지를 반환한다.
     */
    @GetMapping("/messages")
    fun getMessage(): String {
        val resourceBundle = ResourceBundle.getBundle(basename, LocaleContextHolder.getLocale(), YamlResourceBundle.Control.INSTANCE)
        val keys = resourceBundle.keys
        var msgJson = JsonObject()
        while (keys.hasMoreElements()) {
            val key = keys.nextElement()
            msgJson.addProperty(key, resourceBundle.getString(key))
        }
        return msgJson.toString()
    }
}