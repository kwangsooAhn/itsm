package com.brainz.framework.scheduling.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bwfSchedTaskMst")
public class ScheduleTask implements Serializable {

    private static final long serialVersionUID = -6693358225759255228L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long taskId;
    
    @Column(name = "taskType")
    private String taskType;
    
    @Column(name = "executeClass")
    private String executeClass;
    
    @Column(name = "executeQuery")
    private String executeQuery;
    
    @Column(name = "executeCycleType")
    private String executeCycleType;
    
    @Column(name = "executeCyclePeriod")
    private Long executeCyclePeriod;
    
    @Column(name = "cronExpression")
    private String cronExpression;

    @Override
    public String toString() {
        return "ScheduleTask [taskId=" + taskId + ", taskType=" + taskType + ", executeClass=" + executeClass + ", executeQuery="
                + executeQuery + ", executeCycleType=" + executeCycleType + ", executeCyclePeriod=" + executeCyclePeriod
                + ", cronExpression=" + cronExpression + "]";
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getExecuteClass() {
        return executeClass;
    }

    public void setexecuteClass(String executeClass) {
        this.executeClass = executeClass;
    }

    public String getExecuteQuery() {
        return executeQuery;
    }

    public void setExecuteQuery(String executeQuery) {
        this.executeQuery = executeQuery;
    }

    public String getExecuteCycleType() {
        return executeCycleType;
    }

    public void setExecuteCycleType(String executeCycleType) {
        this.executeCycleType = executeCycleType;
    }

    public Long getExecuteCyclePeriod() {
        return executeCyclePeriod;
    }

    public void setExecuteCyclePeriod(Long executeCyclePeriod) {
        this.executeCyclePeriod = executeCyclePeriod;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

}
