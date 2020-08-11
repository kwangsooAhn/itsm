package co.brainz.workflow.tag.entity

import co.brainz.workflow.instance.entity.WfInstanceEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "wf_tag_map")
data class WfTagMapEntity(
    @Id
//    @GeneratedValue(generator = "system-uuid")
//    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "tag_id", length = 128)
    val tagId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id")
    var instance: WfInstanceEntity? = null

) : Serializable
