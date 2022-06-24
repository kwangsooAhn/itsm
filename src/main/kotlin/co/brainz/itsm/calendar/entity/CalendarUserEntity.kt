/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.calendar.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "awf_calendar_user")
data class CalendarUserEntity(
    @Id
    @Column(name = "calendar_id", length = 128)
    val calendarId: String = "",

    @Column(name = "calendar_name", length = 100)
    val calendarName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", referencedColumnName = "user_key")
    val owner: AliceUserEntity,

    @Column(name = "create_dt")
    val createDt: LocalDateTime
) : Serializable
