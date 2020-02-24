package co.brainz.workflow.process.dto

import java.io.Serializable

data class WFElementDto(
    var id: String,
    var category: String,
    var type: String,
    var display: String,
    var data: String
) : Serializable
