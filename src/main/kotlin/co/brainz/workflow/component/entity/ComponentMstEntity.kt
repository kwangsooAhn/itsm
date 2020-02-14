package co.brainz.workflow.component.entity

import co.brainz.workflow.form.entity.FormMstEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "wf_comp_mst")
data class ComponentMstEntity(

        @Id @GeneratedValue(generator = "system-uuid")
        @GenericGenerator(name = "system-uuid", strategy = "uuid")
        @Column(name = "comp_id")
        val compId: String,

        @Column(name = "comp_type")
        val compType: String,

        @Column(name = "mapping_id")
        val mappingId: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "form_id")
        val components: FormMstEntity

) : Serializable {
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "attributes")
        val attributes: MutableList<ComponentDataEntity>? = mutableListOf()
}
