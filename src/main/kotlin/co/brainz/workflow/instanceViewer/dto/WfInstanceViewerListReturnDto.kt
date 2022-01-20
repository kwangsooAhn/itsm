package co.brainz.workflow.instanceViewer.dto

import java.io.Serializable

data class WfInstanceViewerListReturnDto(
    //todo: [참조인 관리] 문서 상세 조회시 참조인 정보 추가 진행 시 WfInstanceViewerListDto 생성 하며 진행예정
    var data: List<WfInstanceViewerDetailDto>?,
    var totalCount: Long = 0L
) : Serializable
