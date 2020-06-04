package co.brainz.workflow.engine.tag.entity

import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import java.io.Serializable
import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "wf_tag")
data class WfTagEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "tag_id")
    val tagId: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id")
    var instance: WfInstanceEntity? = null

    ) : Serializable
