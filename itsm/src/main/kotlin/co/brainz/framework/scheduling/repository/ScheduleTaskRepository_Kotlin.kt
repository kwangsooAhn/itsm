package co.brainz.framework.scheduling.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository;

import co.brainz.framework.scheduling.model.ScheduleTask_Kotlin

@Repository
public interface ScheduleTaskRepository_Kotlin : CrudRepository<ScheduleTask_Kotlin, Long> {
	override fun findAll() : MutableList<ScheduleTask_Kotlin>
	
}
