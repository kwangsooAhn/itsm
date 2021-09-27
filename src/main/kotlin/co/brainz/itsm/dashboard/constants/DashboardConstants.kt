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
        INCIDENT("servicedesk.incident"),
        INQUIRY("servicedesk.inquiry"),
        REQUEST("servicedesk.request"),
        ETC("servicedesk.etc")
    }
}
