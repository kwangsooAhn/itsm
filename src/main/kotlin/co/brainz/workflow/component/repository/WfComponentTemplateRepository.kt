/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.repository

import co.brainz.workflow.component.entity.WfComponentTemplateEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfComponentTemplateRepository : JpaRepository<WfComponentTemplateEntity, String> {
    fun existsByTemplateName(templateName: String): Boolean
}
