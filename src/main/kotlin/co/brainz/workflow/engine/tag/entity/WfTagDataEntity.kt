package co.brainz.workflow.engine.tag.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "wf_tag_data")
data class WfTagDataEntity(
    @Id
    @Column(name = "tag_id")
    val tagId: String,

    @Column(name = "tag_content")
    val tagContent: String
) : Serializable
