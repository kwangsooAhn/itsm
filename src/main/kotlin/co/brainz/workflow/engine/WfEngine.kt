package co.brainz.workflow.engine

import co.brainz.workflow.engine.component.repository.WfComponentDataRepository
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.document.service.WfDocumentService
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.form.repository.WfFormRepository
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import co.brainz.workflow.engine.process.service.WfProcessService
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.engine.token.service.WfTokenActionService
import co.brainz.workflow.engine.token.service.WfTokenElementService
import co.brainz.workflow.engine.token.service.WfTokenService
import org.springframework.stereotype.Service

@Service
class WfEngine(private val wfFormRepository: WfFormRepository,
               private val wfProcessRepository: WfProcessRepository,
               private val wfComponentRepository: WfComponentRepository,
               private val wfComponentDataRepository: WfComponentDataRepository,
               private val wfDocumentRepository: WfDocumentRepository,
               private val wfInstanceRepository: WfInstanceRepository,
               private val wfTokenRepository: WfTokenRepository,
               private val wfTokenDataRepository: WfTokenDataRepository,
               private val wfFormService: WfFormService,
               private val wfActionService: WfActionService,
               private val wfTokenElementService: WfTokenElementService,
               private val wfInstanceService: WfInstanceService,
               private val wfElementService: WfElementService,
               private val wfTokenActionService: WfTokenActionService) {

    /**
     * Form Engine.
     */
    fun form(): WfFormService {
        return WfFormService(wfFormRepository, wfComponentRepository, wfComponentDataRepository)
    }

    /**
     * Process Engine.
     */
    fun process(): WfProcessService {
        return WfProcessService(wfProcessRepository)
    }

    /**
     * Document Engine.
     */
    fun document(): WfDocumentService {
        return WfDocumentService(wfFormService, wfActionService, wfDocumentRepository, wfInstanceRepository, wfProcessRepository, wfFormRepository)
    }

    /**
     * Instance Engine.
     */
    fun instance(): WfInstanceService {
        return WfInstanceService(wfInstanceRepository)
    }

    /**
     * Token Engine.
     */
    fun token(): WfTokenService {
        return WfTokenService(wfDocumentRepository, wfTokenRepository, wfTokenDataRepository, wfInstanceService, wfFormService, wfActionService, wfElementService, wfTokenElementService, wfTokenActionService)
    }

}
