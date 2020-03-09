package co.brainz.workflow.engine

import co.brainz.workflow.component.repository.ComponentDataRepository
import co.brainz.workflow.component.repository.ComponentRepository
import co.brainz.workflow.document.repository.DocumentRepository
import co.brainz.workflow.document.service.WFDocumentService
import co.brainz.workflow.element.repository.ElementRepository
import co.brainz.workflow.element.service.WFElementService
import co.brainz.workflow.form.repository.FormRepository
import co.brainz.workflow.form.service.Form
import co.brainz.workflow.form.service.WFFormService
import co.brainz.workflow.instance.repository.InstanceRepository
import co.brainz.workflow.instance.service.WFInstanceService
import co.brainz.workflow.process.repository.ProcessRepository
import co.brainz.workflow.process.service.WFProcessService
import co.brainz.workflow.token.repository.TokenDataRepository
import co.brainz.workflow.token.repository.TokenRepository
import co.brainz.workflow.token.service.WFTokenService
import org.springframework.stereotype.Service

@Service
class WFEngine(private val formRepository: FormRepository,
               private val processRepository: ProcessRepository,
               private val elementRepository: ElementRepository,
               private val componentRepository: ComponentRepository,
               private val componentDataRepository: ComponentDataRepository,
               private val wfFormService: WFFormService,
               private val documentRepository: DocumentRepository,
               private val instanceRepository: InstanceRepository,
               private val tokenRepository: TokenRepository,
               private val tokenDataRepository: TokenDataRepository,
               private val wfInstanceService: WFInstanceService,
               private val wfElementService: WFElementService) {

    /**
     * Form Engine.
     */
    fun form(): Form {
        return WFFormService(formRepository, componentRepository, componentDataRepository)
        //return FormDummy()
    }

    /**
     * Process Engine.
     */
    fun process(): WFProcessService {
        return WFProcessService(processRepository)
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
        return WFInstanceService(instanceRepository)
    }

    /**
     * Token Engine.
     */
    fun token(): WFTokenService {
        return WFTokenService(documentRepository, tokenRepository, tokenDataRepository, wfInstanceService, wfElementService)
    }

}
