package co.brainz.workflow.process.service

public class WfProcessManager {
	
	private lateinit var accessList : MutableList<Any>
	
	fun createQuery() : WfProcessManager {
		accessList.clear();
		return this;
	}
	
	fun accessGroup(group : String) : WfProcessManager {
		accessList.add(group)
		return this;
	}
	
	fun accessAsignee(asignee : String) : WfProcessManager {
		accessList.add(asignee)
		return this;
	}
}
