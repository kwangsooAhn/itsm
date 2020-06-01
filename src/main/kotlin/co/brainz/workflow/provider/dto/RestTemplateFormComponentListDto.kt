package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

/**
 * Form과 관련되어 가장 기본이 되는 DTO.
 * Form ID를 기준으로 속해 있는 컴포넌트 리스트를 반환
 *
 * @author Jung Hee Chan
 * @since 2020-05-28
 * @param id 찾고자 하는 Form Id.
 * @param components 화면에서 사용하는 데이터로 가공한 컴포넌트 리스트
 */
class RestTemplateFormComponentListDto(
    var formId: String = "",
    var name: String = "",
    var status: String? = "",
    var desc: String? = "",
    var updateDt: LocalDateTime?,
    var updateUserKey: String? = "",
    var createDt: LocalDateTime?,
    var createUserKey: String? = "",
    val components: MutableList<ComponentDetail> = mutableListOf()
) : Serializable

class ComponentDetail(
    var componentId: String = "",
    val type: String = "",
    var values: MutableList<LinkedHashMap<String, Any>>? = mutableListOf(),
    val dataAttribute: LinkedHashMap<String, Any> = LinkedHashMap<String, Any>(),
    var display: LinkedHashMap<String, Any> = LinkedHashMap<String, Any>(),
    var label: LinkedHashMap<String, Any> = LinkedHashMap<String, Any>(),
    var validate: LinkedHashMap<String, Any> = LinkedHashMap<String, Any>()
) : Serializable
