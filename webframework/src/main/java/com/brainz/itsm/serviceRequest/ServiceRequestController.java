package com.brainz.itsm.serviceRequest;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.brainz.workflow.process.ProcessHist;

@Controller
public class ServiceRequestController {
    
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