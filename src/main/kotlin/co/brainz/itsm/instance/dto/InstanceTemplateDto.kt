/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.dto

import java.io.Serializable
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.workflow.document.entity.WfDocumentEntity

data class InstanceTemplateDto(
    var instanceId: String,
    var document: WfDocumentEntity? = null,
    var instanceStatus: String? = "",
    var instanceCreateUserKey: AliceUserEntity? = null
):Serializable
