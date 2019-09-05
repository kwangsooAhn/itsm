package co.brainz.framework.sample.scheduling.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.brainz.framework.scheduling.model.ScheduleTask;
import co.brainz.framework.scheduling.repository.ScheduleTaskRepository;
import co.brainz.framework.scheduling.service.ScheduleTaskService;

@RestController
public class SchedulingController {
    
    @Autowired
    ScheduleTaskService scheduleTaskService;
    
    @Autowired
    ScheduleTaskRepository scheduleTaskRepository;
    
    @PostMapping("/sample/scheduling/add")
    public String addTask(@RequestBody ScheduleTask task) {
        ScheduleTask savedTask = this.scheduleTaskRepository.save(task);
        this.scheduleTaskService.addTaskToScheduler(savedTask);
        return "스케줄링 TASK 추가";
    }
    
    @PostMapping("/sample/scheduling/update")
    public String updateTask(@RequestBody ScheduleTask task) {
        ScheduleTask savedTask = this.scheduleTaskRepository.save(task);
        this.scheduleTaskService.removeTaskFromScheduler(savedTask.getTaskId());
        this.scheduleTaskService.addTaskToScheduler(savedTask);
        return "스케줄링  갱신";
    }
    
    @GetMapping("/sample/scheduling/delete/{id}")
    public String removeTask(@PathVariable long id) {
        this.scheduleTaskRepository.deleteById(id);
        this.scheduleTaskService.removeTaskFromScheduler(id);
        return "스케줄링  삭제";
    }
}
