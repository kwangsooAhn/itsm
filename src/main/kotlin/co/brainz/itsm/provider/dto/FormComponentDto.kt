package co.brainz.itsm.provider.dto

import java.io.Serializable

data class FormComponentDto(
        var form: FormViewDto,
        var components: MutableList<ComponentDto> = mutableListOf()
): Serializable

