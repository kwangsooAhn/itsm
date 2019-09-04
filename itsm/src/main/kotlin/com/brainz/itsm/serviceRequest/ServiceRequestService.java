package com.brainz.itsm.serviceRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.brainz.workflow.process.ProcessHist;
import com.brainz.workflow.process.ProcessHistRepository;
import com.brainz.workflow.process.ProcessMst;
import com.brainz.workflow.process.ProcessMstRepository;
import com.brainz.workflow.assignee.Assignee;

@Repository(value = "serviceRequestService")
public class ServiceRequestService {
    @Autowired
    ProcessHistRepository processHistRepository;
    
    @Autowired
    ProcessMstRepository processMstRepository;
    
    public List<ProcessHist> getProcessList() {
        return processHistRepository.findAll();
    }

    public void setData() {
    	Assignee assignee = new Assignee();
        ProcessMst processMst = new ProcessMst("test01",assignee.hello());
        processMstRepository.save(processMst);
        
        ProcessHist processHist = new ProcessHist("dfb6b01cb09a4e8aa25d7f071924411c","0.0.1","proc.status.publish");
        processHist.setProcessMst(processMst);
        processHistRepository.save(processHist);
    }
}
