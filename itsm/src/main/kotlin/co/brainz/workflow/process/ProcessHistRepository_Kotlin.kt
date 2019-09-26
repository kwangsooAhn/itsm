
package co.brainz.workflow.process

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessHistRepository_Kotlin : JpaRepository<ProcessHist_Kotlin, String> {
	fun findByProcId(procId : String) : MutableList<ProcessHist_Kotlin>
	override fun findAll() : MutableList<ProcessHist_Kotlin>
}
