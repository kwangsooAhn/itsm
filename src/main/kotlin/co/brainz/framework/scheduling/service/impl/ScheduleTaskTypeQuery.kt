/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.scheduling.service.impl

import co.brainz.framework.scheduling.entity.AliceScheduleHistoryEntity
import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.repository.AliceScheduleHistoryRepository
import co.brainz.framework.util.AliceUtil
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class ScheduleTaskTypeQuery(
    private val jdbcTemplate: JdbcTemplate,
    private val aliceScheduleHistoryRepository: AliceScheduleHistoryRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getRunnable(taskInfo: AliceScheduleTaskEntity, isImmediately: Boolean): Runnable? {
        return Runnable {
            taskInfo.executeQuery?.let { executeQuery(taskInfo, isImmediately) }
        }
    }

    /**
     * 쿼리를 실행한다.
     *
     * @param executeQuery 실행쿼리
     */
    fun executeQuery(taskInfo: AliceScheduleTaskEntity, isImmediately: Boolean) {
        val executeQuery = taskInfo.executeQuery
        if (!executeQuery.isNullOrEmpty()) {
            var errorMsg = ""
            var isSuccess = true
            try {
                jdbcTemplate.execute(executeQuery)
                logger.info("The query has been executed. [{}]", executeQuery)
            } catch (e: Exception) {
                isSuccess = false
                errorMsg = AliceUtil().printStackTraceToString(e)
            }

            aliceScheduleHistoryRepository.save(
                AliceScheduleHistoryEntity(
                    historySeq = 0L,
                    taskId = taskInfo.taskId,
                    executeTime = LocalDateTime.now(),
                    immediatelyExecute = isImmediately,
                    result = isSuccess,
                    errorMessage = errorMsg
                )
            )
        }
    }
}
