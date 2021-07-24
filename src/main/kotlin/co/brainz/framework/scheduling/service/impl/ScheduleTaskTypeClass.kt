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
import org.springframework.stereotype.Component

@Component
class ScheduleTaskTypeClass(
    private val aliceScheduleHistoryRepository: AliceScheduleHistoryRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getRunnable(taskInfo: AliceScheduleTaskEntity, isImmediately: Boolean): Runnable? {
        val runClass = this.getRunClass(taskInfo)
        return Runnable {
            var isSuccess = true
            var errorMsg = ""
            try {
                runClass?.run()
            } catch (e: Exception) {
                isSuccess = false
                errorMsg = AliceUtil().printStackTraceToString(e)
                logger.error("Failed to load class. [{}]", taskInfo.executeClass)
            }
            aliceScheduleHistoryRepository.save(
                AliceScheduleHistoryEntity(
                    historySeq = 0L,
                    taskId = taskInfo.taskId,
                    immediatelyExecute = isImmediately,
                    executeTime = LocalDateTime.now(),
                    result = isSuccess,
                    errorMessage = errorMsg
                )
            )
        }
    }

    fun getRunClass(taskInfo: AliceScheduleTaskEntity): Runnable? {
        var runnable: Runnable? = null
        try {
            val taskClass = Class.forName(taskInfo.executeClass)
                .asSubclass(Runnable::class.java)
            val args = this.getArgs(taskInfo.args)
            runnable = if (args.isEmpty()) {
                taskClass.getDeclaredConstructor().newInstance()
            } else {
                taskClass.getDeclaredConstructor(Any::class.java).newInstance(args)
            }
        } catch (e: Exception) {
            logger.error("Failed to load class. [{}]", taskInfo.executeClass)
            e.printStackTrace()
        }
        return runnable
    }

    fun getArgs(args: String?): MutableList<Any> {
        val argsList = mutableListOf<Any>()
        if (args != null && args.isNotEmpty()) {
            args.split(",").forEach {
                argsList.add(it.trim())
            }
        }
        return argsList
    }
}
