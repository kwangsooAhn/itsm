/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.dto

import java.io.Serializable

class CIsExcelDto(
    var ciId: String = "",
    var ciNo: String? = null,
    var ciName: String? = null,
    var ciDesc: String? = null,
    var typeName: String? = null,
    var interlink: Boolean? = false
) : Serializable
