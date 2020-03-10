package co.brainz.workflow.component.entity

import co.brainz.workflow.form.entity.FormEntity
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "wf_component")
data class ComponentEntity(

        @Id
        @Column(name = "component_id", length = 128)
        val componentId: String,

        @Column(name = "component_type", length = 100)
        val componentType: String,

        @Column(name = "mapping_id", length = 128)
        var mappingId: String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "form_id")
        val components: FormEntity

) : Serializable {
        @OneToMany(fetch = FetchType.LAZY, mappedBy = "attributes", cascade = [CascadeType.REMOVE])
        val attributes: MutableList<ComponentDataEntity>? = mutableListOf()
}

