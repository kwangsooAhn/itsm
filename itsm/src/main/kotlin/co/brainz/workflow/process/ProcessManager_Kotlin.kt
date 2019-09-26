package co.brainz.workflow.process

public class ProcessManager_Kotlin {
	
	private lateinit var accessList : MutableList<Any>
	
	fun createQuery() : ProcessManager_Kotlin {
		accessList.clear();
		return this;
	}
	
	fun accessGroup(group : String) : ProcessManager_Kotlin {
		accessList.add(group)
		return this;
	}
	
	fun accessAsignee(asignee : String) : ProcessManager_Kotlin {
		accessList.add(asignee)
		return this;
	}
}
