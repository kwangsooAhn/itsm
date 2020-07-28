/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.framework.configuration

import co.brainz.framework.util.AliceMessageSource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 자바스크립트 전용 다국어 클래스
 */
@RestController
@RequestMapping("/i18n")
class AliceI18nRestController(private val messageSource: AliceMessageSource) {

    /**
     * 전체 메시지를 반환한다.
     */
    @GetMapping("/messages")
    fun getMessage(): String {
        return messageSource.getAllMessage()
    }
}
