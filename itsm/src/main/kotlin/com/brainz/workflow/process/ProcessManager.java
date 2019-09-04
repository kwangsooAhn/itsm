package com.brainz.workflow.process;

import java.util.List;

public class ProcessManager {

    private List<Object> accessList;
    
    public ProcessManager createQuery() {
        accessList.clear();
        return this;
    }

    public ProcessManager accessGroup(String group) {
        accessList.add(group);
        return this;
    }

    public ProcessManager accessAsignee(String asignee) {
        accessList.add(asignee);
        return this;
    }
    
//    public List<Process> list() {
//        List<Process> processList = new List<Process>();
//        { new Process("test1"), new Process("test2") };
//            
//    }

}
