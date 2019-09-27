package co.brainz.framework.sample.scheduling.controller

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import co.brainz.framework.scheduling.model.ScheduleTask
import co.brainz.framework.scheduling.repository.ScheduleTaskRepository
import co.brainz.framework.scheduling.service.ScheduleTaskService
@RestController
public class ScehdulingController {
	
	@Autowired
	lateinit var scheduleTaskService : ScheduleTaskService
	
	@Autowired
	lateinit var scheduleTaskRepository : ScheduleTaskRepository
	
	@PostMapping("/sample/scheduling/add")
    public fun addTask(@RequestBody task : ScheduleTask):String{
		var savedTask : ScheduleTask
		savedTask = this.scheduleTaskRepository.save(task)
		this.scheduleTaskService.addTaskToScheduler(savedTask)
		return "스케줄링 TASK 추가"
	}

	@PostMapping("/sample/scheduling/update")
    public fun updateTask(@RequestBody task:ScheduleTask):String {
	    var savedTask : ScheduleTask 
		savedTask = this.scheduleTaskRepository.save(task)
		this.scheduleTaskService.removeTaskFromScheduler(savedTask.taskId)
        this.scheduleTaskService.addTaskToScheduler(savedTask)
		return "스케줄링 갱신";
	}
	
	@GetMapping("/sample/scheduling/delete/{id}")
    public fun removeTask(@PathVariable id : Long): String {
		this.scheduleTaskRepository.deleteById(id);
        this.scheduleTaskService.removeTaskFromScheduler(id);
		return "스케줄링 삭제";
	}
}
