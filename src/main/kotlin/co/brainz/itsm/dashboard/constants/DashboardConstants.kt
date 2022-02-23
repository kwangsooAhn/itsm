package co.brainz.itsm.dashboard.constants

object DashboardConstants {
    /**
     * 대시보드 업무통계내 통계 유형 ex> 처리할 문서 / 진행중 문서/ 월간 처리한 문서/ 전체 처리한 문서
     */
    enum class StatisticType(val code: String) {
        TODO("todo"),
        RUNNING("running"),
        MONTHDONE("monthDone"),
        DONE("done")
    }

    /**
     *  대시보드 업무통계내 문서 그룹 ex> 장애신고/ 단순 문의/ 서비스 요청/ 기타
     */
    enum class DocumentGroup(val code: String) {
        INCIDENT("document.group.incident"),
        INQUIRY("document.group.inquiry"),
        REQUEST("document.group.request"),
        ETC("document.group.etc")
    }

    /**
     * 개인현황판 기본 템플릿
     */
    const val DEFAULT_TEMPLATE_ID = "template-001"

    enum class TemplateComponent(val code: String) {
        ORGANIZATION_CHART("requestStatusByOrganization.chart"),
        STATUS_USER_LIST("requestStatusByUser.list"),
        ORGANIZATION_LIST("requestListByOrganization.list")
    }

    /**
     * 컴포넌트 item 키
     */
    enum class ComponentItemKey(val code: String) {
        TITLE("title"),
        WIDTH("width"),
        DATA_TYPE("dataType")
    }

    /**
     * 컴포넌트 item 타입
     */
    enum class ComponentItemType(val code: String) {
        FIELD("field"),
        MAPPING("mapping")
    }

    /**
     * 컴포넌트 조회 컬럼
     */
    enum class Column(val code: String) {
        DOCUMENT_NAME("document_name"),
        DOCUMENT_NO("document_no"),
        INSTANCE_STATUS("instance_status")
    }
}
