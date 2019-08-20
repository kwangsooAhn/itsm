package com.brainz.workflow.process;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name="wf_proc_mst")
public class ProcessMst {
    
    public ProcessMst() { }
    public ProcessMst(String procKey, String procName) {
        this.procKey = procKey;
        this.procName = procName;
    }

    @Id
    @Column(name="procKey")
    private String procKey;
    
    @Column(name = "procName")
    private String procName;
    
    @Column(name = "activeProcId")
    private String activeProcId;
    
//    @OneToMany(mappedBy = "processMst", cascade=CascadeType.ALL)
//    private List<ProcessHist> procHists = new ArrayList<ProcessHist>();
    
    public String getProcKey() {
        return this.procKey;
    }
    
    public void setProcKey(String procKey) {
        this.procKey = procKey;
    }
    
    public String getProcName() {
        return this.procName;
    }
    
    public void setProcName(String procName) {
        this.procName = procName;
    }
    
    public String getActiveProcId() {
        return this.activeProcId;
    }
    
    public void setActiveProcId(String activeProcId) {
        this.activeProcId = activeProcId;
    }
}
