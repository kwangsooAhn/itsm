/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@SequenceGenerator(
    name = "schedule_history_seq",
    sequenceName = "schedule_history_seq",
    initialValue = 1,
    allocationSize = 1
)
@Table(name = "awf_scheduled_history")
data class AliceScheduleHistoryEntity(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedule_history_seq")
    @Column(name = "history_seq", insertable = true, updatable = false)
    val historySeq: Long,

    @Column(name = "task_id", length = 128, nullable = false)
    var taskId: String,

    @Column(name = "immediately_execute")
    var immediatelyExecute: Boolean? = false,

    @Column(name = "execute_time", length = 128)
    var executeTime: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "result")
    var result: Boolean?,

    @Column(name = "error_message")
    var errorMessage: String? = null
) : Serializable
