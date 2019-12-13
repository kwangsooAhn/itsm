package co.brainz.framework.scheduling.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository;
import co.brainz.framework.scheduling.entity.ScheduleTaskEntity

@Repository
public interface ScheduleTaskRepository: CrudRepository<ScheduleTaskEntity, Long> {
    override fun findAll(): MutableList<ScheduleTaskEntity>
}
