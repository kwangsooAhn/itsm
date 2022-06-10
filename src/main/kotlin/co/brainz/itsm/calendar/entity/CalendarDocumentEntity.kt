/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.calendar.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_calendar_document")
data class CalendarDocumentEntity(
    @Id
    @Column(name = "calendar_id", length = 128)
    val calendarId: String = "",

    @Column(name = "calendar_name", length = 100)
    val calendarName: String,

    @Column(name = "create_dt")
    val createDt: LocalDateTime
) : Serializable
