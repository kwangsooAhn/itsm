package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.token.repository.WfCandidateRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository

class WfCommonStartEventTokenManager(
    override val wfElementService: WfElementService,
    override val wfInstanceService: WfInstanceService,
    override val wfInstanceRepository: WfInstanceRepository,
    override val wfElementRepository: WfElementRepository,
    override val wfTokenRepository: WfTokenRepository,
    override val wfTokenDataRepository: WfTokenDataRepository,
    override val wfCandidateRepository: WfCandidateRepository
) : WfTokenManager() {

}
