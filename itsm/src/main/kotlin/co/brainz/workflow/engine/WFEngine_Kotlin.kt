package co.brainz.workflow.engine

import co.brainz.workflow.process.ProcessManager_Kotlin;

public class WFEngine_Kotlin {
	
	companion object {
		public fun getProcessManager() : ProcessManager_Kotlin {
		
			return ProcessManager_Kotlin()	
		}
	}
}