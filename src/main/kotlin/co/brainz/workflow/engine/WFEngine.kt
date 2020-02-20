package co.brainz.workflow.engine

import co.brainz.workflow.component.repository.ComponentDataRepository
import co.brainz.workflow.component.repository.ComponentMstRepository
import co.brainz.workflow.form.repository.FormMstRepository
import co.brainz.workflow.form.service.Form
import co.brainz.workflow.form.service.WFFormService
import co.brainz.workflow.process.repository.ProcessMstRepository
import co.brainz.workflow.process.service.WFProcessService
import co.brainz.workflow.document.service.WFDocumentService
import org.springframework.stereotype.Service

@Service
class WFEngine(private val formMstRepository: FormMstRepository,
               private val processMstRepository: ProcessMstRepository,
               private val componentMstRepository: ComponentMstRepository,
               private val componentDataRepository: ComponentDataRepository,
               private val wfFormService: WFFormService,
               private val wfProcessService: WFProcessService) {

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
        return WFProcessService(processMstRepository)
    }

    /**
     * Document Engine.
     */
    fun document(): WFDocumentService {
        return WFDocumentService(wfFormService, wfProcessService, processMstRepository)
    }

}
