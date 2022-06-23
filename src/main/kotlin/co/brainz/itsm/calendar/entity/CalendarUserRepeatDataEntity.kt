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
@Table(name = "awf_calendar_user_repeat_data")
class CalendarUserRepeatDataEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "data_id", length = 128)
    val dataId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repeat_id")
    val repeat: CalendarUserRepeatEntity,

    @Column(name = "repeat_start_dt")
    val repeatStartDt: LocalDateTime,

    @Column(name = "repeat_end_dt")
    val repeatEndDt: LocalDateTime? = null,

    @Column(name = "repeat_type", length = 64)
    val repeatType: String?,

    @Column(name = "repeat_value", length = 64)
    val repeatValue: String? = null,

    @Column(name = "repeat_title", length = 200)
    val repeatTitle: String,

    @Column(name = "repeat_contents")
    val repeatContents: String? = null,

    @Column(name = "all_day_yn")
    val allDayYn: Boolean,

    @Column(name = "start_dt")
    val startDt: LocalDateTime,

    @Column(name = "end_dt")
    var endDt: LocalDateTime,

    @Column(name = "create_dt")
    val createDt: LocalDateTime? = null
) : Serializable
