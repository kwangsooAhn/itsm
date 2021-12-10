package co.brainz.cmdb.dto

import co.brainz.framework.tag.dto.AliceTagDto
import java.io.Serializable

class CIListExcelDto(
    var ciNo: String? = null,
    var ciName: String? = null,
    var ciDesc: String? = null,
    var typeName: String? = null,
    var interlink: Boolean? = false,
    var ciTags: List<AliceTagDto>? = null
) : Serializable