package co.brainz.workflow.provider.dto

/**
 * 폼 관련 연동 데이터 구조 통일을 위해서 새롭게 작성된 DTO
 * 최초 신청서 작성이나 처리할문서 내용을 받는 DTO
 * 받은 이후 동작은 WF 구조 변경에 따라 대폭 수정될 예정이라 현재는 아래 구조로 받기만 하고 Controller에서 기존 구조로 변경함.
 * 이후 아래 구조를 Service 레벨에서 직접 사용하도록 변경할 필요가 있음.
 *
 * @author Jung Hee Chan
 * @since 2020-06-01
 */
import java.io.Serializable

data class RestTemplateTokenDataUpdateDto(
    var documentId: String? = null,
    var tokenId: String = "",
    var isComplete: Boolean = true,
    var assigneeId: String? = null,
    var assigneeType: String? = null,
    val action: String? = null,
    var componentData: List<RestTemplateTokenDataDto>? = null
) : Serializable
