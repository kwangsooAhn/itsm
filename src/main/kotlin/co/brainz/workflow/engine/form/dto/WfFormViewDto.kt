package co.brainz.workflow.engine.form.dto

import java.io.Serializable

data class WfFormViewDto(
        var id: String = "",
        var name: String = "",
        var desc: String? = null,
        var status: String? = null
): Serializable

