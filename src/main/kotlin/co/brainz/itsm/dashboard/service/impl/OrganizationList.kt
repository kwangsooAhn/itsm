/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.itsm.dashboard.dto.TemplateComponentConfig

class OrganizationList : co.brainz.itsm.dashboard.service.impl.TemplateComponent {
    override fun getResult(component: TemplateComponentConfig): Any {
        //return "bbb"
        return emptyArray<String>()
        //TODO("Not yet implemented")
    }
}
