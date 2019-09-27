package co.brainz.workflow.process

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
public data class ProcessHist(
	@Id var procId : String,
    @Column(name = "procVersion") var procVersion : String? = null,
    @Column(name = "procStatus") var procStatus : String ? = null
     ) : Serializable {
	
	companion object {
		private final val serialVersionUID : Long = -2343243243242432341L;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)], targetEntity=ProcessMst::class)
    @JoinColumn(name = "procKey")
	lateinit var processMst : ProcessMst
	
	@JvmName("processMstGet")
	fun getProcessMst():ProcessMst {
     return this.processMst
    }
	
    @JvmName("processMstSet")
    fun setProcessMst(processMst:ProcessMst) {
      this.processMst = processMst
    }
}
