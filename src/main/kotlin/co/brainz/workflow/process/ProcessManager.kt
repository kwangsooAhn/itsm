package co.brainz.workflow.process

public class ProcessManager {
	
	private lateinit var accessList : MutableList<Any>
	
	fun createQuery() : ProcessManager {
		accessList.clear();
		return this;
	}
	
	fun accessGroup(group : String) : ProcessManager {
		accessList.add(group)
		return this;
	}
	
	fun accessAsignee(asignee : String) : ProcessManager {
		accessList.add(asignee)
		return this;
	}
}
