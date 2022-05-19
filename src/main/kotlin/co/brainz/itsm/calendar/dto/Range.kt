package co.brainz.itsm.calendar.dto

import java.io.Serializable
import java.time.LocalDateTime

data class Range(
    var from: LocalDateTime? = null,
    var to: LocalDateTime? = null
) : Serializable
