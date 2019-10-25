package co.brainz.workflow.process

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository

@Repository
public interface ProcessHistRepository : JpaRepository<ProcessHist, String> {
	fun findByProcId(procId : String) : MutableList<ProcessHist>
	override fun findAll() : MutableList<ProcessHist>
}
