/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.scheduling.service.impl

import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ScheduleTaskTypeJar {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getRunnable(taskInfo: AliceScheduleTaskEntity): Runnable? {
        var runnable: Runnable? = null

        //jar 파일을 찾아서 실행한다.
        //1. 경로 찾기 (아이콘처럼 외부)

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
