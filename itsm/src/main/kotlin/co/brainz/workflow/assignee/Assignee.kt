package co.brainz.workflow.assignee

class Assignee {
    fun hello():String {
		val assigneeTest = AssigneeTest();
        return assigneeTest.test() + "Hellow World";
    }
}