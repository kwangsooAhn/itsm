package co.brainz.framework.certification.dto

import java.io.Serializable

data class AliceMailDto(
    var subject: String?,
    var content: String?,
    var from: String,
    var fromName: String?,
    var to: ArrayList<String>,
    var cc: ArrayList<String>?,
    var bcc: ArrayList<String>?
) : Serializable
