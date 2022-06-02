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
@Table(name = "awf_calendar_repeat_custom_data")
class CalendarRepeatCustomDataEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "custom_id", length = 128)
    val customId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_id")
    val repeatData: CalendarRepeatDataEntity,

    @Column(name = "data_index")
    val dataIndex: Int,

    @Column(name = "custom_type", length = 64)
    val customType: String,

    @Column(name = "schedule_title", length = 200)
    val scheduleTitle: String,

    @Column(name = "schedule_contents")
    val scheduleContents: String? = null,

    @Column(name = "all_day_yn")
    val allDayYn: Boolean,

    @Column(name = "start_dt")
    val startDt: LocalDateTime,

    @Column(name = "end_dt")
    val endDt: LocalDateTime
) : Serializable
