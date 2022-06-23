/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.calendar.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_calendar_user_schedule")
data class CalendarUserScheduleEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "schedule_id", length = 128)
    val scheduleId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    val calendar: CalendarUserEntity,

    @Column(name = "schedule_title", length = 200)
    var scheduleTitle: String,

    @Column(name = "schedule_contents")
    var scheduleContents: String? = null,

    @Column(name = "all_day_yn")
    var allDayYn: Boolean,

    @Column(name = "start_dt")
    var startDt: LocalDateTime,

    @Column(name = "end_dt")
    var endDt: LocalDateTime,

    @Column(name = "create_dt")
    val createDt: LocalDateTime? = null,

    @Column(name = "update_dt")
    val updateDt: LocalDateTime? = null
) : Serializable
