package co.brainz.workflow.instance.dto

import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.process.entity.WfProcessEntity
import java.io.Serializable

data class WfInstanceListDocumentDto(
    val documentId: String,
    var documentType: String,
    var documentName: String,
    var documentStatus: String? = null,
    var documentDesc: String? = null,
    var documentColor: String?,
    var process: WfProcessEntity,
    var form: WfFormEntity,
    var numberingRule: NumberingRuleEntity,
    var documentIcon: String? = null
) : Serializable
