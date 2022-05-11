package co.brainz.itsm.archive.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "awf_archive")
class ArchiveEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "archive_id")
    var archiveId: String = "",

    @Column(name = "archive_seq", insertable = false, updatable = false)
    var archiveSeq: Long = 0,

    @Column(name = "archive_category")
    var archiveCategory: String = "",

    @Column(name = "archive_title")
    var archiveTitle: String = "",

    @Column(name = "views")
    var views: Int = 0

) : Serializable, AliceMetaEntity()
