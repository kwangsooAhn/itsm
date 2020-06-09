package co.brainz.workflow.component.constants

object WfComponentConstants {
    // topicDisplay 가 true 이면 목록 화면에 출력하는 대상 component type.
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
            // 목록 화면에 출력 대상이 되는 component type list 를 반환.
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
