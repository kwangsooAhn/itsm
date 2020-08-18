package co.brainz.workflow.element.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "wf_element_script_data")
@IdClass(WfElementScriptDataPk::class)
data class WfElementScriptDataEntity(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", insertable = false, updatable = false)
    val element: WfElementEntity,

    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "script_id", length = 256)
    val scriptId: String,

    @Column(name = "script_value")
    val scriptValue: String?

) : Serializable

data class WfElementScriptDataPk(
    val element: String = "",
    val scriptId: String = ""
) : Serializable
