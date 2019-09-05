package co.brainz.framework.scheduling.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.brainz.framework.scheduling.model.ScheduleTask;

@Repository
public interface ScheduleTaskRepository extends CrudRepository<ScheduleTask, Long> {
    List<ScheduleTask> findAll();
}
