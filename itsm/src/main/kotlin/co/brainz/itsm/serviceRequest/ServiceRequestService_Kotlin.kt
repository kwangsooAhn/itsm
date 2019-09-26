package co.brainz.itsm.serviceRequest

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Repository;
import co.brainz.workflow.assignee.Assignee;
import co.brainz.workflow.process.ProcessHist_Kotlin;
import co.brainz.workflow.process.ProcessHistRepository_Kotlin;
import co.brainz.workflow.process.ProcessMstRepository_Kotlin;
import org.slf4j.LoggerFactory
import co.brainz.workflow.process.ProcessMst_Kotlin

@Repository(value = "serviceRequestService")
public open class ServiceRequestService_Kotlin {
	
	companion object {
	private val logger = LoggerFactory.getLogger(ServiceRequestService_Kotlin::class.java)
	}
	
	fun Logging() : Unit{
		logger.info("INFO{ }","ServiceRequestService_Kotlin")
	}
	 @Autowired
     lateinit var processHistRepository : ProcessHistRepository_Kotlin
	
	 @Autowired
     lateinit var processMstRepository : ProcessMstRepository_Kotlin
	
	 public fun getProcessList() : MutableList<ProcessHist_Kotlin>{
        return processHistRepository.findAll();
	 }
	
	 public fun setData() : Unit{
	    var assignee : Assignee = Assignee()
		var processMst : ProcessMst_Kotlin = ProcessMst_Kotlin("test01",assignee.hello())
		processMstRepository.save(processMst)
		 
		var processHist : ProcessHist_Kotlin = ProcessHist_Kotlin("dfb6b01cb09a4e8aa25d7f071924411c","0.0.1","proc.status.publish");
		processHist.processMst = processMst
		processHistRepository.save(processHist)
		  	 
	 }
}
