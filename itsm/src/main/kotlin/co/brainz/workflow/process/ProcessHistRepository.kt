package co.brainz.workflow.process

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessHistRepository : JpaRepository<ProcessHist, String> {
	fun findByProcId(procId : String) : MutableList<ProcessHist>
	override fun findAll() : MutableList<ProcessHist>
}
