package co.brainz.workflow.engine

import co.brainz.workflow.form.repository.FormRepository
import co.brainz.workflow.form.service.Form
import co.brainz.workflow.form.service.FormProvider
import org.springframework.stereotype.Controller

@Controller
class WFEngine() {

	/**
	 * Form Engine.
	 */
	fun form(formRepository: FormRepository): Form {
		return FormProvider(formRepository)
		//return FormDummy()
	}
}
