package co.brainz.workflow.engine.document.repository.specification

import co.brainz.framework.specification.AliceSpecification
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Join
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root


/**
 * 신청서 검색시 사용할 Specification 클래스
 */
class WfSearchDocuments(private val searchDto: RestTemplateDocumentSearchListDto) :
    AliceSpecification<WfDocumentEntity> {

    override fun toPredicate(
        root: Root<WfDocumentEntity>,
        query: CriteriaQuery<*>,
        cb: CriteriaBuilder
    ): Predicate? {

        val documentNameOrDescription = searchDto.searchDocuments ?: ""
        val documentStatus = searchDto.searchDocumentStatus ?: ""
        val processName = searchDto.searchProcessName ?: ""
        val formName = searchDto.searchFormName ?: ""

        // 검색어 없으면 전체 조회.
        if (documentNameOrDescription == "" && documentStatus == "" && processName == "" && formName == "") {
            return null
        }

        // 서치를 시작해 보자 가즈아 ~ !!!
        val predicate: MutableList<Predicate> = mutableListOf()
        // 신청서 이름, 설명으로 조회
        if (documentNameOrDescription != "") {
            val searchDocument: MutableList<Predicate> = mutableListOf()
            searchDocument.add(super.like(cb, root.get("documentName"), documentNameOrDescription))
            searchDocument.add(super.like(cb, root.get("documentDesc"), documentNameOrDescription))
            predicate.add(cb.or(*searchDocument.toTypedArray()))
        }

        // 신청서에 연결된 프로세스 이름으로 조회
        if (processName != "") {
            val process: Join<WfDocumentEntity, WfProcessEntity> = root.join("process")
            predicate.add(super.like(cb, process.get("processName"), processName))
        }

        // 신청서에 연결된 문서양식 이름으로 조회
        if (formName != "") {
            val form: Join<WfDocumentEntity, WfFormEntity> = root.join("form")
            predicate.add(super.like(cb, form.get("formName"), formName))
        }

        return cb.and(*predicate.toTypedArray())
    }
}
