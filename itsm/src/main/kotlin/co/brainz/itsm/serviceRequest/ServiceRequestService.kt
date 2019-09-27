package co.brainz.itsm.serviceRequest

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import co.brainz.workflow.assignee.Assignee;
import co.brainz.workflow.process.ProcessHist;
import co.brainz.workflow.process.ProcessHistRepository;
import co.brainz.workflow.process.ProcessMstRepository;
import org.slf4j.LoggerFactory
import co.brainz.workflow.process.ProcessMst;

@Repository(value = "serviceRequestService")
public open class ServiceRequestService {
	
	companion object {
	private val logger = LoggerFactory.getLogger(ServiceRequestService::class.java)
	}
	
	fun Logging() : Unit{
		logger.info("INFO{ }","ServiceRequestService")
	}
	 @Autowired
     lateinit var processHistRepository : ProcessHistRepository
	
	 @Autowired
     lateinit var processMstRepository : ProcessMstRepository
	
	 public fun getProcessList() : MutableList<ProcessHist>{
        return processHistRepository.findAll();
	 }
	
	 public fun setData() : Unit{
	    var assignee : Assignee = Assignee()
		var processMst : ProcessMst = ProcessMst("test01",assignee.hello())
		processMstRepository.save(processMst)
		 
		var processHist : ProcessHist = ProcessHist("dfb6b01cb09a4e8aa25d7f071924411c","0.0.1","proc.status.publish");
		processHist.processMst = processMst
		processHistRepository.save(processHist)
		  	 
	 }
}
