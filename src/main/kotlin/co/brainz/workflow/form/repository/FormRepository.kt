package co.brainz.workflow.form.repository

import co.brainz.workflow.form.entity.FormEntity
import org.springframework.data.jpa.repository.JpaRepository

interface FormRepository: JpaRepository<FormEntity, String>, FormRepositoryCustom {

}
