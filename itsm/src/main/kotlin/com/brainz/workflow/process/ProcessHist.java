package com.brainz.workflow.process;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wfProcHist")
public class ProcessHist implements Serializable {

    private static final long serialVersionUID = -2343243243242432341L;

    public ProcessHist() { }
    public ProcessHist(String procId, String procVersion, String procStatus) {
        this.procId = procId;
        this.procVersion = procVersion;
        this.procStatus = procStatus;
    }
    
    @Id
    private String procId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity=ProcessMst.class)
    @JoinColumn(name = "procKey")
    private ProcessMst processMst;

    @Column(name = "procVersion")
    private String procVersion;

    @Column(name = "procStatus")
    private String procStatus;

    public String getProcId() {
        return this.procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }
    
    public ProcessMst getProcessMst() { 
        return this.processMst; 
    }
     
    public void setProcessMst(ProcessMst processMst) { 
        this.processMst = processMst; 
    }
    
//    @Override
//    public String toString() {
//        return String.format("{'proc_key':'%s', 'proc_version':'%s', 'proc_status':'%s', 'proc_id':'%s'}", procKey,
//                procVersion, procStatus, procId);
//    }

    public String getProcVersion() {
        return this.procVersion;
    }

    public void setProcVersion(String procVersion) {
        this.procVersion = procVersion;
    }

    public String getProcStatus() {
        return this.procStatus;
    }

    public void setProcStatus(String procStatus) {
        this.procStatus = procStatus;
    }
}
