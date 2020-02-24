package co.brainz.workflow.process.entity

import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@GenericGenerator(name = "system-uuid", strategy = "uuid")
@Table(name = "wf_elem_mst")
data class ElementMstEntity(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @Column(name = "elem_id", length = 256)
    val elemId: String,

    @Column(name = "proc_id", length = 128)
    var procId: String,

    @Column(name = "elem_type", length = 100)
    var elemType: String,

    @Column(name = "elem_name", length = 256)
    var elemName: String,

    @Column(name = "elem_desc", length = 1024)
    var elemDesc: String,

    @Column(name = "noti_email")
    var notiEmail: Boolean,

    @Column(name = "elem_config")
    var elemConfig: String,

    @Column(name = "display_info")
    var displayInfo: String
) : Serializable
