package co.brainz.itsm.cmdb.ciIcon.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@SequenceGenerator(
    // 시퀀스 제너레이터 이름
    name = "cmdb_ci_icon_file_seq_gen",
    // 시퀀스 이름
    sequenceName = "cmdb_ci_icon_file_seq",
    // 시작값
    initialValue = 16,
    // 메모리를 통해 할당할 범위 사이즈
    allocationSize = 1
)
@Table(name = "cmdb_ci_icon")
data class CIIconEntity(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cmdb_ci_icon_file_seq_gen")
    @Column(name = "file_seq")
    var fileSeq: Long,

    @Column(name = "file_name", length = 512)
    var fileName: String,

    @Column(name = "file_name_extension", length = 128)
    var fileNameExtension: String,

    @Column(name = "uploaded_location", length = 512)
    var uploadedLocation: String?,

    @Column(name = "editable")
    var editable: Boolean = true
) : Serializable, AliceMetaEntity()
