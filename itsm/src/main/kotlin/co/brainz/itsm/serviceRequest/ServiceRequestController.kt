package co.brainz.itsm.serviceRequest


import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import co.brainz.framework.sample.logging.controller.LoggingController;
import co.brainz.workflow.process.ProcessHist;

@Controller
class ServiceRequestController {
	
	companion object {
	private val logger = LoggerFactory.getLogger(ServiceRequestController::class.java)
	}
	
	fun Logging() : Unit{
		logger.info("INFO{ }","ServiceRequestController")
	}
	
	@Resource(name = "serviceRequestService")
    private lateinit var serviceRequestService : ServiceRequestService
	
	@GetMapping("/processList")
    public fun serviceRequest(model : Model) : String {
		
	    serviceRequestService.setData();
		
		val processList : MutableList<ProcessHist> = serviceRequestService.getProcessList();
		 
		model.addAttribute("processList",processList);
		
		return "common/layout"	
	}
	
	@GetMapping("/writeCustomerRequest")
    public fun writeCustomerRequest(model : Model) : String {
		
		serviceRequestService.setData();
		
		val processList : MutableList<ProcessHist> = serviceRequestService.getProcessList()
		
		model.addAttribute("processList",processList);
		
		return "itsm/writeCutomerRequest"
	}
}

