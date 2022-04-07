/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CITemplateDto(
    val ciType: String,
    val ciName: String,
    val ciDesc: String,
    val attributeMap: LinkedHashMap<String, String> = linkedMapOf()
) : Serializable
