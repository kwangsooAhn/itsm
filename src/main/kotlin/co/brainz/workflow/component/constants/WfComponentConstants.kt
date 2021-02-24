/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.constants

object WfComponentConstants {

    // component type code 값.
    enum class ComponentTypeCode(val code: String) {
        TEXT("inputbox"),
        TEXT_AREA("textbox"),
        SELECT("dropdown"),
        RADIO("radio"),
        CHECKBOX("checkbox"),
        LABEL("label"),
        IMAGE("image"),
        DIVIDER("divider"),
        DATE("date"),
        TIME("time"),
        DATETIME("datetime"),
        FILEUPLOAD("fileupload"),
        CUSTOM_CODE("custom-code"),
        DR_TABLE("dynamic-row-table"),
        ACCORDION_START("accordion-start"),
        ACCORDION_END("accordion-end"),
        CI("ci");
    }

    // component Value Type 구분
    enum class ComponentValueType(val code: String) {
        STRING_SEPARATOR("separator"),
        STRING("string");
    }

    // topicDisplay 가 true 이면 목록 화면에 출력하는 대상 component type.
    enum class ComponentType(val code: String, val topicDisplay: Boolean) {
        TEXT("inputbox", true),
        TEXT_AREA("textbox", false),
        SELECT("dropdown", false),
        RADIO("radio", false),
        CHECKBOX("checkbox", false),
        LABEL("label", false),
        IMAGE("image", false),
        DIVIDER("divider", false),
        DATE("date", false),
        TIME("time", false),
        DATETIME("datetime", false),
        FILEUPLOAD("fileupload", false),
        CUSTOM_CODE("custom-code", false),
        DR_TABLE("dynamic-row-table", false),
        ACCORDION_START("accordion-start", false),
        ACCORDION_END("accordion-end", false),
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
