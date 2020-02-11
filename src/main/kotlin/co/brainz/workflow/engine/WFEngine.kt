package co.brainz.workflow.engine

import co.brainz.workflow.form.repository.FormMstMstRepository
import co.brainz.workflow.form.service.Form
import co.brainz.workflow.form.service.WFFormService
import co.brainz.workflow.process.repository.ProcessMstRepository
import co.brainz.workflow.process.service.WFProcessService
import org.springframework.stereotype.Service

@Service
class WFEngine(private val formMstRepository: FormMstMstRepository, private val processMstRepository: ProcessMstRepository) {

    /**
     * Form Engine.
     */
    fun form(): Form {
        return WFFormService(formMstRepository)
        //return FormDummy()
    }

    /**
     * Process Engine.
     */
    fun process(): WFProcessService {
        return WFProcessService(processMstRepository)
    }
}
