package co.brainz.itsm.serviceRequest;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import co.brainz.framework.sample.logging.controller.LoggingController;
import co.brainz.workflow.process.ProcessHist;

@Controller
public class ServiceRequestController {
	private static Logger logger = LoggerFactory.getLogger(ServiceRequestController.class);
	
    @Resource(name = "serviceRequestService")
    private ServiceRequestService serviceRequestService;
    
    @GetMapping("/processList")
    public String serviceRequest(Model model) {       
        serviceRequestService.setData();
        
        List<ProcessHist> processList = serviceRequestService.getProcessList();
        
        model.addAttribute("processList",processList);
        
        return "common/layout";
    }    
    
    @GetMapping("/writeCustomerRequest")
    public String writeCustomerRequest(Model model) {        
        
        serviceRequestService.setData();
        
        List<ProcessHist> processList = serviceRequestService.getProcessList();
        
        model.addAttribute("processList",processList);
        
        return "itsm/writeCustomerRequest";
    }   
}