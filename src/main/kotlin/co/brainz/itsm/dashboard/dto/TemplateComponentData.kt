
package co.brainz.itsm.dashboard.dto

import java.io.Serializable

data class TemplateComponentData(
    val key: String,
    val title: String,
    val result: Any? = null
) : Serializable
