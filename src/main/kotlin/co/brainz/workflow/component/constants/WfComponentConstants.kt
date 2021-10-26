/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.constants

object WfComponentConstants {

    // component type code 값.
    enum class ComponentTypeCode(val code: String) {
        TEXT("inputBox"),
        TEXT_AREA("textArea"),
        SELECT("dropdown"),
        RADIO("radio"),
        CHECKBOX("checkBox"),
        LABEL("label"),
        IMAGE("image"),
        DIVIDER("divider"),
        DATE("date"),
        TIME("time"),
        DATETIME("dateTime"),
        FILEUPLOAD("fileUpload"),
        CUSTOM_CODE("customCode"),
        DR_TABLE("dynamicRowTable"),
        CI("ci");
    }

    // component Value Type 구분
    enum class ComponentValueType(val code: String) {
        STRING_SEPARATOR("separator"),
        STRING("string");
    }

    // topicDisplay 가 true 이면 목록 화면에 출력하는 대상 component type.
    enum class ComponentType(val code: String, val topicDisplay: Boolean) {
        TEXT("inputBox", true),
        TEXT_AREA("textArea", false),
        SELECT("dropdown", false),
        RADIO("radio", false),
        CHECKBOX("checkBox", false),
        LABEL("label", true),
        IMAGE("image", false),
        DIVIDER("divider", false),
        DATE("date", false),
        TIME("time", false),
        DATETIME("dateTime", false),
        FILEUPLOAD("fileUpload", false),
        CUSTOM_CODE("customCode", false),
        DR_TABLE("dynamicRowTable", false),
        CI("ci", false);

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
