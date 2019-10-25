package co.brainz.framework.scheduling.model

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "bwfSchedTaskMst")
public data class ScheduleTask(
	@Id @GeneratedValue(strategy = GenerationType.AUTO) var taskId : Long,
    @Column(name = "taskType") var taskType : String,
	@Column(name = "executeClass") var executeClass : String,
	@Column(name = "executeQuery") var executeQuery : String,
	@Column(name = "executeCycleType") var executeCycleType : String,
	@Column(name = "executeCyclePeriod") var executeCyclePeriod : Long,
	@Column(name = "cronExpression") var cronExpression : String) : Serializable{
	companion object {
		private final val serialVersionUID : Long = -6693358225759255228L;
	}
	
	override fun toString() : String {
		return (
				"ScheduleTask [taskId= $taskId ,askType= $taskType , executeClass= $executeClass , executeQuery= $executeQuery , executeCycleType= $executeCycleType , executeCyclePeriod= $executeCyclePeriod , cronExpression= $cronExpression]"
			  )
		
	}
}
