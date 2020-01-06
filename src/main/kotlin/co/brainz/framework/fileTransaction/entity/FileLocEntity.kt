package co.brainz.framework.fileTransaction.entity

import org.springframework.format.annotation.DateTimeFormat
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Table

@Entity
@SequenceGenerator(
        name = "awf_file_loc_seq_gen",     //시퀀스 제너레이터 이름
        sequenceName = "awf_file_loc_seq", //시퀀스 이름
        initialValue = 1,                  //시작값
        allocationSize = 1                 //메모리를 통해 할당할 범위 사이즈
)
@Table(name = "awf_file_loc")
data class FileLocEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "awf_file_loc_seq_gen")
        @Column(name = "seq") var fileSeq: Long,
        @Column(name = "file_owner") var fileOwner: String?,
        @Column(name = "uploaded") var uploaded: Boolean?,
        @Column(name = "uploaded_location") var uploadedLocation: String?,
        @Column(name = "random_name", length = 1024) var randomName: String?,
        @Column(name = "origin_name", length = 1024) var originName: String?,
        @Column(name = "file_size") var fileSize: Long?,
        @Column(name = "sort") var sort: Int?,
        @Column(name = "create_userid") var createUserid: String?,
        @Column(name = "update_userid") var updateUserid: String?,
        @Column(name = "create_dt") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var createDt: LocalDateTime?,
        @Column(name = "update_dt") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") var updateDt: LocalDateTime?
) : Serializable