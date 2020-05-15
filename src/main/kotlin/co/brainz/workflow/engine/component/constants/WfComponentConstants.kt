package co.brainz.workflow.engine.component.constants

object WfComponentConstants {
    enum class ComponentType(val code: String, val topicDisplay: Boolean) {
        TEXT("text", true),
        TEXT_AREA("textarea", false),
        SELECT("select", false),
        RADIO("radio", false),
        CHECKBOX("checkbox", false),
        LABEL("label", false),
        IMAGE("image", false),
        DIVIDER("divider", false),
        DATE("date", false),
        TIME("time", false),
        DATETIME("datetime", false),
        FILEUPLOAD("fileupload", false),
        CUSTOM_CODE("custom-code", false);

        companion object {
            fun getComponentTypeForTopicDisplay(): ArrayList<String> {
                val topicComponentTypeForDisplay = ArrayList<String>()
                enumValues<ComponentType>().forEach {
                    if (it.topicDisplay) topicComponentTypeForDisplay.add(it.code)
                }
                return topicComponentTypeForDisplay
            }
        }
    }
}
