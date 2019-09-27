package co.brainz.workflow.engine

import co.brainz.workflow.process.ProcessManager;

public class WFEngine {
	
	companion object {
		public fun getProcessManager() : ProcessManager {
		
			return ProcessManager()	
		}
	}
}