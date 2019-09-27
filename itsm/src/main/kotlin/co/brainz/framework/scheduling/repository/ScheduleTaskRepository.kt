package co.brainz.framework.scheduling.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository;
import co.brainz.framework.scheduling.model.ScheduleTask

@Repository
public interface ScheduleTaskRepository : CrudRepository<ScheduleTask, Long> {
	override fun findAll() : MutableList<ScheduleTask>
	
}
