package co.brainz.workflow.engine

import co.brainz.workflow.component.repository.ComponentDataRepository
import co.brainz.workflow.component.repository.ComponentMstRepository
import co.brainz.workflow.document.repository.DocumentRepository
import co.brainz.workflow.document.service.WFDocumentService
import co.brainz.workflow.element.service.WFElementService
import co.brainz.workflow.form.repository.FormMstRepository
import co.brainz.workflow.form.service.Form
import co.brainz.workflow.form.service.WFFormService
import co.brainz.workflow.instance.repository.InstanceMstRepository
import co.brainz.workflow.instance.service.WFInstanceService
import co.brainz.workflow.process.repository.ElementDataRepository
import co.brainz.workflow.process.repository.ElementMstRepository
import co.brainz.workflow.process.repository.ProcessMstRepository
import co.brainz.workflow.process.service.WFProcessService
import co.brainz.workflow.token.repository.TokenDataRepository
import co.brainz.workflow.token.repository.TokenMstRepository
import co.brainz.workflow.token.service.WFTokenService
import org.springframework.stereotype.Service

@Service
class WFEngine(private val formMstRepository: FormMstRepository,
               private val processMstRepository: ProcessMstRepository,
               private val componentMstRepository: ComponentMstRepository,
               private val componentDataRepository: ComponentDataRepository,
               private val wfFormService: WFFormService,
               private val documentRepository: DocumentRepository,
               private val instanceMstRepository: InstanceMstRepository,
               private val tokenMstRepository: TokenMstRepository,
               private val tokenDataRepository: TokenDataRepository,
               private val wfInstanceService: WFInstanceService,
               private val wfElementService: WFElementService) {

    /**
     * Form Engine.
     */
    fun form(): Form {
        return WFFormService(formMstRepository, componentMstRepository, componentDataRepository)
        //return FormDummy()
    }

    /**
     * Process Engine.
     */
    fun process(): WFProcessService {
        return WFProcessService(processMstRepository, elementMstRepository)
    }

    /**
     * Document Engine.
     */
    fun document(): WFDocumentService {
        return WFDocumentService(wfFormService, documentRepository)
    }

    /**
     * Instance Engine.
     */
    fun instance(): WFInstanceService {
        return WFInstanceService(instanceMstRepository)
    }

    /**
     * Token Engine.
     */
    fun token(): WFTokenService {
        return WFTokenService(documentRepository, tokenMstRepository, tokenDataRepository, wfInstanceService, wfElementService)
    }

}
