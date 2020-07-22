package co.brainz.workflow.tag.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "wf_tag")
data class WfTagEntity(
    @Id
    @Column(name = "tag_id", length = 128)
    val tagId: String,

    @Column(name = "tag_content", length = 256)
    val tagContent: String
) : Serializable
