package co.brainz.workflow.instanceViewer.constants

object WfInstanceViewerConstants {

    /**
     * ViewType status.
     */

    enum class ViewType(val value: String) {
        REGISTER("register"),
        MODIFY("modify"),
        DELETE("delete")
    }
}