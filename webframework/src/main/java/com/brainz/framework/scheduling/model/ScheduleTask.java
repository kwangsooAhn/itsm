package com.brainz.framework.scheduling.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "scheduleTaskInfo")
public class ScheduleTask implements Serializable {

    private static final long serialVersionUID = -6693358225759255228L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "taskType")
    private String taskType;
    
    @Column(name = "taskClass")
    private String taskClass;
    
    @Column(name = "executeQuery")
    private String executeQuery;
    
    @Column(name = "runCycleType")
    private String runCycleType;
    
    @Column(name = "milliseconds")
    private Long milliseconds;
    
    @Column(name = "cronExpression")
    private String cronExpression;

    @Override
    public String toString() {
        return "ScheduleTask [id=" + id + ", taskType=" + taskType + ", taskClass=" + taskClass + ", executeQuery="
                + executeQuery + ", runCycleType=" + runCycleType + ", milliseconds=" + milliseconds
                + ", cronExpression=" + cronExpression + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public String getExecuteQuery() {
        return executeQuery;
    }

    public void setExecuteQuery(String executeQuery) {
        this.executeQuery = executeQuery;
    }

    public String getRunCycleType() {
        return runCycleType;
    }

    public void setRunCycleType(String runCycleType) {
        this.runCycleType = runCycleType;
    }

    public Long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(Long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

}
