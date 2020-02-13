package co.brainz.workflow.component.entity

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "wf_comp_mst")
data class ComponentMstEntity(

        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "comp_id")
        val compId: String,

        @Column(name = "form_id")
        val formId: String,

        @Column(name = "comp_type")
        val compType: String,

        @Column(name = "comp_config")
        val compConfig: String,

        @Column(name = "display_info")
        val displayInfo: String

) : Serializable
