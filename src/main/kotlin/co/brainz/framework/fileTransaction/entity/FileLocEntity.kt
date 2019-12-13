package co.brainz.framework.fileTransaction.entity

import javax.persistence.*

@Entity
@SequenceGenerator(
        name="awf_file_loc_seq_gen",     //시퀀스 제너레이터 이름
        sequenceName="awf_file_loc_seq", //시퀀스 이름
        initialValue=1,                  //시작값
        allocationSize=1                 //메모리를 통해 할당할 범위 사이즈
)
@Table(name = "awfFileLoc")
data class FileLocEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="awf_file_loc_seq_gen")
        @Column(name="seq")
        var fileSeq: Long,
        @Column(name="type", length=128)
        var type: String?,
        @Column(name="task", length=128)
        var task: String?,
        @Column(name="name", length=1024)
        var name: String?,
        @Column(name="originName", length=1024)
        var originName: String?,
        @Column(name="size")
        var size: Long?,
        @Column(name="location", length=1024)
        var location: String?,
        @Column(name="createUser", length=128)
        var createUser: String?,
        @Column(name="isUpload")
        var isUpload: Boolean?,
        @Column(name="sort")
        var sort: Int?
)