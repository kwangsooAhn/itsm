package co.brainz.workflow.process

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="wf_proc_mst")
public data class ProcessMst(
    @Column(name = "procName") var procName : String ? = null,
	@Column(name = "activeProcId") var activeProcId : String ? = null
    ) {
	@Id
	@Column(name="procKey")
	lateinit var procKey : String
	
    @JvmName("procKeyGet")
    fun getProcKey():String {
      return this.procKey
    }
	
    @JvmName("procKeySet")
    fun setProcKey(procKey:String) {
      this.procKey = procKey
    } 
}
