/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.framework.util

import com.google.gson.JsonObject
import java.util.ResourceBundle
import net.rakugakibox.util.YamlResourceBundle
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

/**
 * message properties 관련 기능 클래스
 */
@Service
class AliceMessageSource {

    @Value("\${spring.messages.basename}")
    private var basename = ""

    private fun getResourceBundle(): ResourceBundle {
        return ResourceBundle.getBundle(basename, LocaleContextHolder.getLocale(), YamlResourceBundle.Control.INSTANCE)
    }

    /**
     *  코드 범위 [codeRange]에서 입력 받은 [input]을 가지는 코드값을 찾아서 코드를 리턴한다.
     *
     *  e.g.
     *  faq.category[codeRange]를 가지는 코드들에서 입력 받은 "전"[input]을 가지는 코드 faq.category.all, faq.category.setting 을 리턴한다.
     *  faq:
     *    category:
     *      all: "전체"
     *      etc: "기타"
     *      setting: "전체 설정"
     *      techSupport: "기술지원"
     */
    fun getUserInputToCodes(codeRange: String, input: String): MutableList<String> {
        val codes: MutableMap<String, String> = mutableMapOf()
        val resourceBundle = this.getResourceBundle()
        val keys = resourceBundle.keys
        while (keys.hasMoreElements()) {
            val key = keys.nextElement()
            if (key.contains(codeRange)) {
                codes[key.replace("^common.code.".toRegex(), "")] = resourceBundle.getString(key)
            }
        }
        return codes.filter { it.value.contains(input) }.keys.toMutableList()
    }

    /**
     * 메시지 프로퍼티에 있는 전체 메시지를 리턴한다.
     */
    fun getAllMessage(): String {
        val resourceBundle = this.getResourceBundle()
        val keys = resourceBundle.keys
        val msgJson = JsonObject()
        while (keys.hasMoreElements()) {
            val key = keys.nextElement()
            msgJson.addProperty(key, resourceBundle.getString(key))
        }
        return msgJson.toString()
    }

    /**
     * 메시지 프로퍼티에서 [key]를 찾아 해당 메시지를 리턴한다.
     */
    fun getMessage(key: String): String {
        val resourceBundle = this.getResourceBundle()
        return resourceBundle.getString(key)
    }
}
