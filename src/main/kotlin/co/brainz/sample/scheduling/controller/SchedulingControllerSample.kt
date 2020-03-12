package co.brainz.sample.scheduling.controller

import co.brainz.framework.scheduling.entity.AliceScheduleTaskEntity
import co.brainz.framework.scheduling.repository.AliceScheduleTaskRepository
import co.brainz.framework.scheduling.service.AliceScheduleTaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
public class SchedulingControllerSample {
    
    @Autowired
    lateinit var aliceScheduleTaskService: AliceScheduleTaskService
    
    @Autowired
    lateinit var aliceScheduleTaskRepository: AliceScheduleTaskRepository
    
    @PostMapping("/sample/scheduling/add")
    public fun addTask(@RequestBody task: AliceScheduleTaskEntity): String {
        val savedTask: AliceScheduleTaskEntity = this.aliceScheduleTaskRepository.save(task)
        this.aliceScheduleTaskService.addTaskToScheduler(savedTask)
        return "스케줄링 TASK 추가"
    }

    @PostMapping("/sample/scheduling/update")
    public fun updateTask(@RequestBody task: AliceScheduleTaskEntity): String {
        val savedTask: AliceScheduleTaskEntity = this.aliceScheduleTaskRepository.save(task)
        this.aliceScheduleTaskService.removeTaskFromScheduler(savedTask.taskId)
        this.aliceScheduleTaskService.addTaskToScheduler(savedTask)
        return "스케줄링 갱신"
    }
    
    @GetMapping("/sample/scheduling/delete/{id}")
    public fun removeTask(@PathVariable id: Long): String {
        this.aliceScheduleTaskRepository.deleteById(id)
        this.aliceScheduleTaskService.removeTaskFromScheduler(id)
        return "스케줄링 삭제"
    }
}
