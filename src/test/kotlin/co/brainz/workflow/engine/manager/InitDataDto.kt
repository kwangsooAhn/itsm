package co.brainz.workflow.engine.manager

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.numberingRule.entity.NumberingRuleEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.process.entity.WfProcessEntity
import java.io.Serializable

data class InitDataDto(
    var users: MutableList<AliceUserEntity>? = null,
    var numberingRule: MutableList<NumberingRuleEntity>? = null,
    var forms: MutableList<WfFormEntity>? = null,
    var processes: MutableList<WfProcessEntity>? = null,
    var documents: MutableList<WfDocumentEntity>? = null,
    var instance: WfInstanceEntity? = null
) : Serializable
