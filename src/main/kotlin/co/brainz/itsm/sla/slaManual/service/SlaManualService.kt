/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.slaManual.service

import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.repository.AliceScheduleHistoryRepository
import co.brainz.framework.scheduling.repository.AliceScheduleTaskRepository
import co.brainz.framework.scheduling.service.AliceScheduleTaskService
import co.brainz.framework.scheduling.service.impl.ScheduleTaskTypeClass
import co.brainz.framework.scheduling.service.impl.ScheduleTaskTypeJar
import co.brainz.framework.scheduling.service.impl.ScheduleTaskTypeQuery
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.scheduler.constants.SchedulerConstants
import co.brainz.itsm.scheduler.dto.SchedulerDto
import co.brainz.itsm.scheduler.dto.SchedulerHistorySearchCondition
import co.brainz.itsm.scheduler.dto.SchedulerListDto
import co.brainz.itsm.scheduler.dto.SchedulerListReturnDto
import co.brainz.itsm.scheduler.dto.SchedulerSearchCondition
import java.io.File
import java.nio.file.Paths
import java.time.Instant
import javax.transaction.Transactional
import kotlin.math.ceil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Service

@Service
class SlaManualService(
) {
}
