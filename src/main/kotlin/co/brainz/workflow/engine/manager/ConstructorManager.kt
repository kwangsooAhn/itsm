package co.brainz.workflow.engine.manager

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.service.WfTokenMappingValue
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.token.repository.WfCandidateRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import org.springframework.stereotype.Component

@Component
class ConstructorManager(
    private val wfElementService: WfElementService,
    private val wfInstanceService: WfInstanceService,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfElementRepository: WfElementRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfCandidateRepository: WfCandidateRepository,
    private val wfTokenMappingValue: WfTokenMappingValue
) {

    fun getElementService(): WfElementService {
        return this.wfElementService
    }

    fun getInstanceService(): WfInstanceService {
        return this.wfInstanceService
    }

    fun getInstanceRepository(): WfInstanceRepository {
        return this.wfInstanceRepository
    }

    fun getElementRepository(): WfElementRepository {
        return this.wfElementRepository
    }

    fun getTokenRepository(): WfTokenRepository {
        return this.wfTokenRepository
    }

    fun getTokenDataRepository(): WfTokenDataRepository {
        return this.wfTokenDataRepository
    }

    fun getCandidateRepository(): WfCandidateRepository {
        return this.wfCandidateRepository
    }

    fun getTokenMappingValue(): WfTokenMappingValue {
        return this.wfTokenMappingValue
    }
}
