/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.scheduling.service.impl

import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class ScheduleTaskTypeQuery(
    private val jdbcTemplate: JdbcTemplate
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getRunnable(taskInfo: AliceScheduleTaskEntity): Runnable? {
        return Runnable {
            taskInfo.executeQuery?.let { executeQuery(it) }
        }
    }

    /**
     * 쿼리를 실행한다.
     *
     * @param executeQuery 실행쿼리
     */
    fun executeQuery(executeQuery: String) {
        jdbcTemplate.execute(executeQuery)
        logger.info("The query has been executed. [{}]", executeQuery)
    }
}
