package co.brainz.workflow.engine

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.numbering.repository.AliceNumberingRuleRepository
import co.brainz.workflow.engine.comment.repository.WfCommentRepository
import co.brainz.workflow.engine.comment.service.WfCommentService
import co.brainz.workflow.engine.component.repository.WfComponentDataRepository
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.component.service.WfComponentService
import co.brainz.workflow.engine.document.repository.WfDocumentDisplayRepository
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.document.service.WfDocumentService
import co.brainz.workflow.engine.element.repository.WfElementDataRepository
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.form.repository.WfFormRepository
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import co.brainz.workflow.engine.process.service.WfProcessService
import co.brainz.workflow.engine.process.service.simulation.WfProcessSimulator
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.engine.token.service.WfTokenElementService
import co.brainz.workflow.engine.token.service.WfTokenService
import co.brainz.workflow.engine.tag.repository.WfTagRepository
import co.brainz.workflow.engine.tag.repository.WfTagDataRepository
import co.brainz.workflow.engine.tag.service.WfTagService
import org.springframework.stereotype.Service

@Service
class WfEngine(
    private val wfFormRepository: WfFormRepository,
    private val wfProcessRepository: WfProcessRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfComponentDataRepository: WfComponentDataRepository,
    private val wfElementRepository: WfElementRepository,
    private val wfElementDataRepository: WfElementDataRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val wfDocumentDisplayRepository: WfDocumentDisplayRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfFormService: WfFormService,
    private val wfActionService: WfActionService,
    private val wfTokenElementService: WfTokenElementService,
    private val wfCommentRepository: WfCommentRepository,
    private val aliceNumberingRuleRepository: AliceNumberingRuleRepository,
    private val wfProcessSimulator: WfProcessSimulator,
    private val wfElementService: WfElementService,
    private val aliceUserRepository: AliceUserRepository,
    private val wfCommentService: WfCommentService,
	private val wfTagRepository: WfTagRepository,
	private val wfTagDataRepository: WfTagDataRepository,
	private val wfTagService: WfTagService
	) {

    /**
     * Form Engine.
     */
    fun form(): WfFormService {
        return WfFormService(wfFormRepository, wfComponentRepository, wfComponentDataRepository)
    }

    /**
     * component Engine.
     */
    fun component(): WfComponentService {
        return WfComponentService(wfComponentRepository, wfComponentDataRepository)
    }

    /**
     * Process Engine.
     */
    fun process(): WfProcessService {
        return WfProcessService(wfProcessRepository, wfProcessSimulator)
    }

    /**
     * Document Engine.
     */
    fun document(): WfDocumentService {
        return WfDocumentService(
            wfFormService,
            wfActionService,
            wfDocumentRepository,
            wfDocumentDisplayRepository,
            wfInstanceRepository,
            wfProcessRepository,
            wfFormRepository,
            wfComponentRepository,
            wfComponentDataRepository,
            wfElementRepository,
            wfElementDataRepository,
            aliceNumberingRuleRepository,
            wfElementService
        )
    }

    /**
     * Instance Engine.
     */
    fun instance(): WfInstanceService {
        return WfInstanceService(
            wfInstanceRepository,
            wfTokenRepository,
            wfCommentService,
			wfTagService
		)
    }

    /**
     * Token Engine.
     */
    fun token(): WfTokenService {
        return WfTokenService(
            wfTokenRepository,
            wfTokenDataRepository,
            wfDocumentDisplayRepository,
            wfFormService,
            wfActionService,
            wfTokenElementService
        )
    }

    /**
     * Comment Engine.
     */
    fun comment(): WfCommentService {
        return WfCommentService(wfCommentRepository, wfInstanceRepository, aliceUserRepository)
    }

    /**
     * Tag Engine.
     */
    fun tag(): WfTagService {
        return WfTagService(wfTagRepository, wfTagDataRepository, wfInstanceRepository)
    }
}
