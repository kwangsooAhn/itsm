package co.brainz.framework.fileTransaction.entity

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
    name = "awf_file_loc_seq_gen",     // 시퀀스 제너레이터 이름
    sequenceName = "awf_file_loc_seq", // 시퀀스 이름
    initialValue = 1,                  // 시작값
    allocationSize = 1                 // 메모리를 통해 할당할 범위 사이즈
)
@Table(name = "awf_file_loc")
data class AliceFileLocEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "awf_file_loc_seq_gen")
    @Column(name = "seq") var fileSeq: Long,
    @Column(name = "file_owner") var fileOwner: String?,
    @Column(name = "uploaded") var uploaded: Boolean?,
    @Column(name = "uploaded_location") var uploadedLocation: String?,
    @Column(name = "random_name") var randomName: String?,
    @Column(name = "origin_name") var originName: String?,
    @Column(name = "file_size") var fileSize: Long?,
    @Column(name = "sort") var sort: Int?

) : Serializable, AliceMetaEntity()
