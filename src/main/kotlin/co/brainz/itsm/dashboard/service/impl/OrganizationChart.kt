/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service.impl

import co.brainz.itsm.dashboard.dto.TemplateComponentConfig
import org.springframework.stereotype.Component

@Component
class OrganizationChart : co.brainz.itsm.dashboard.service.impl.TemplateComponent {
    override fun getResult(component: TemplateComponentConfig): Any {
        return "aaa"
        //TODO("Not yet implemented")
    }

}
