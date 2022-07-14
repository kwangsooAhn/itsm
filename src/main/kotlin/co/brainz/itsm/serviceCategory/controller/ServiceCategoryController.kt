/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.serviceCategory.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/service-category")
class ServiceCategoryController {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val serviceEditPage: String = "service-category/serviceEdit"

    /**
     * 서비스 카테고리 관리 화면 호출
     */
    @GetMapping("/edit")
    fun getServiceEdit(): String {
        return serviceEditPage
    }
}
