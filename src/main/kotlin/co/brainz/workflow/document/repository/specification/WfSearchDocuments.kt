package co.brainz.workflow.document.repository.specification

import co.brainz.framework.numbering.entity.AliceNumberingRuleEntity
import co.brainz.framework.specification.AliceSpecification
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.process.entity.WfProcessEntity
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
        val processName = searchDto.searchProcessName ?: ""
        val formName = searchDto.searchFormName ?: ""
        val documentStatus = searchDto.searchDocumentStatus ?: ""

        root.fetch<WfDocumentEntity, AliceNumberingRuleEntity>("numberingRule")
        val process = root.fetch<WfDocumentEntity, WfProcessEntity>("process") as Join<*, *>
        val form = root.fetch<WfDocumentEntity, WfFormEntity>("form") as Join<*, *>

        // 서치를 시작해 보자 가즈아 ~ !!!
        val predicate: MutableList<Predicate> = mutableListOf()
        // 신청서 이름, 설명으로 조회
        if (documentNameOrDescription != "") {
            val searchDocument: MutableList<Predicate> = mutableListOf()
            searchDocument.add(super.like(cb, root.get("documentName"), documentNameOrDescription))
            searchDocument.add(super.like(cb, root.get("documentDesc"), documentNameOrDescription))
            predicate.add(cb.or(*searchDocument.toTypedArray()))
        }

        if (documentStatus != "") {
            predicate.add(cb.equal(root.get<String>("documentStatus"), documentStatus))
        }

        // 신청서에 연결된 프로세스 이름으로 조회
        if (processName != "") {
            predicate.add(super.like(cb, process.get("processName"), processName))
        }

        // 신청서에 연결된 문서양식 이름으로 조회
        if (formName != "") {
            predicate.add(super.like(cb, form.get("formName"), formName))
        }

        return if (predicate.size == 0) {
            null
        } else {
            cb.and(*predicate.toTypedArray())
        }
    }
}
