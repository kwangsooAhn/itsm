/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CISearchItem(
    val attributeId: String = "",
    val attributeName: String = "",
    val attributeText: String = "",
    val attributeType: String = "",
    val attributeDesc: String = "",
    val attributeValue: String = "",
    var searchValue: String = ""
) : Serializable
