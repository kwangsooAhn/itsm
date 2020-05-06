package co.brainz.itsm.download.entity

import co.brainz.framework.auditor.AliceMetaEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_download")
class DownloadEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "download_id")
    var downloadId: String = "",

    @Column(name = "download_seq", insertable = false, updatable = false)
    var downloadSeq: Long = 0,

    @Column(name = "download_category")
    var downloadCategory: String = "",

    @Column(name = "download_title")
    var downloadTitle: String = "",

    @Column(name = "views")
    var views: Int = 0

) : Serializable, AliceMetaEntity()
