package co.brainz.framework.sample.scheduling.controller

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.brainz.framework.scheduling.model.ScheduleTask_Kotlin
import co.brainz.framework.scheduling.repository.ScheduleTaskRepository_Kotlin
import co.brainz.framework.scheduling.service.ScheduleTaskService_Kotlin
@RestController
public class ScehdulingController_Kotlin {
	
	@Autowired
	lateinit var scheduleTaskService : ScheduleTaskService_Kotlin
	
	@Autowired
	lateinit var scheduleTaskRepository : ScheduleTaskRepository_Kotlin
	
	@PostMapping("/sample/scheduling/add")
    public fun addTask(@RequestBody task : ScheduleTask_Kotlin):String{
		var savedTask : ScheduleTask_Kotlin
		savedTask = this.scheduleTaskRepository.save(task)
		this.scheduleTaskService.addTaskToScheduler(savedTask)
		return "스케줄링 TASK 추가"
	}

	@PostMapping("/sample/scheduling/update")
    public fun updateTask(@RequestBody task:ScheduleTask_Kotlin):String {
	    var savedTask : ScheduleTask_Kotlin 
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
