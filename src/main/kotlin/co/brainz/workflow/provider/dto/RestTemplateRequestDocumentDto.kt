package co.brainz.workflow.provider.dto

import java.io.Serializable

/**
 * 최초 신청서 데이터
 * 신청서 ID를 기준으로 속해 있는 컴포넌트 리스트를 반환.
 *
 * @author Jung Hee Chan
 * @since 2020-05-28
 * @param id 신청서 아이디 (document id)
 * @param components 화면에서 사용하는 데이터로 가공한 컴포넌트 리스트
 */
class RestTemplateRequestDocumentDto(
    var documentId: String = "",
    val form: RestTemplateFormComponentListDto,
    val actions: MutableList<RestTemplateActionDto>? = mutableListOf()
) : Serializable
