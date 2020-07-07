package co.brainz.workflow

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.numbering.entity.AliceNumberingRuleEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.process.entity.WfProcessEntity
import java.io.Serializable

data class InitDataDto(
    var users: MutableList<AliceUserEntity>? = null,
    var numberingRule: MutableList<AliceNumberingRuleEntity>? = null,
    var forms: MutableList<WfFormEntity>? = null,
    var processes: MutableList<WfProcessEntity>? = null,
    var documents: MutableList<WfDocumentEntity>? = null,
    var instance: WfInstanceEntity? = null
) : Serializable
