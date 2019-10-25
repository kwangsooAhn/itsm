package co.brainz.workflow.process

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="wf_proc_mst")
public data class ProcessMst(
	@Id @Column(name = "prcoKey") var procKey : String? = null,
    @Column(name = "procName") var procName : String? = null,
    @Column(name = "activeProcId") var activeProcId : String? = null
    ) {
}
