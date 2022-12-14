/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.itsm.dashboard.dto.TemplateComponentConfig

interface TemplateComponent {
    fun init(option: Map<String, Any>)
    fun getResult(component: TemplateComponentConfig): Any
}
