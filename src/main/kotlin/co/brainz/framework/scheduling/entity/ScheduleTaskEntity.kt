package co.brainz.framework.scheduling.entity

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "awf_scheduled_task_mst")
public data class ScheduleTaskEntity(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var taskId: Long,
    var taskType: String,
    var executeClass: String,
    var executeQuery: String,
    var executeCycleType: String,
    var executeCyclePeriod: Long,
    var cronExpression: String
): Serializable {
    companion object {
        private final const val serialVersionUID = -6693358225759255228L
    }
    override fun toString(): String {
        return (
                "ScheduleTask [taskId= $taskId, askType= $taskType, executeClass= $executeClass, " +
                        "executeQuery= $executeQuery, executeCycleType= $executeCycleType, " +
                        "executeCyclePeriod= $executeCyclePeriod, cronExpression= $cronExpression]"
                )
    }
}
