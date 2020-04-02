package co.brainz.framework.scheduling.repository

import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
public interface AliceScheduleTaskRepository: CrudRepository<AliceScheduleTaskEntity, Long> {
    override fun findAll(): MutableList<AliceScheduleTaskEntity>
}
