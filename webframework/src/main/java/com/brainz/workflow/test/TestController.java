package com.brainz.workflow.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class TestController {
    
    @Resource(name = "testService")
    private TestService testServices;
    
    @GetMapping("/workflow")
    public String custmerRequest(Model model) {        
        //ProcessManager processMgr = BPMEngine.getProcessManager();
//        List<Process> processList = processMgr.createQuery()
//                .accessGroup("itsm")
//                .accessGroup("infraweb")
//                .accessAsignee("hcjung")
//                .list();
//        model.addAttribute("processList", processList);
        model.addAttribute("message","test");
        return "common/layout";
    }
    
}