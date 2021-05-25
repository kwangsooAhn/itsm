/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.scheduling.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_scheduled_task_mst")
data class AliceScheduleTaskEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    var taskId: String,
    var taskName: String,
    var taskDesc: String?,
    var taskType: String,
    var useYn: Boolean,
    var editable: Boolean,
    var executeClass: String?,
    var executeQuery: String?,
    var executeCommand: String?,
    var executeCycleType: String,
    var executeCyclePeriod: Long?,
    var cronExpression: String?,
    var args: String?,
    var src: String?
) : Serializable
