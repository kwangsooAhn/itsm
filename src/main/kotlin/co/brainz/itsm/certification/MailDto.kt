package co.brainz.itsm.certification

import java.io.Serializable

data class MailDto(
        var subject: String?,
        var content: String?,
        var from: String,
        var fromName: String?,
        var to: ArrayList<String>,
        var cc: ArrayList<String>?,
        var bcc: ArrayList<String>?
): Serializable
